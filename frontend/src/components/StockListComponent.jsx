import { useEffect, useState } from "react";
import { TOP_SYMBOLS } from "../../constants";

export default function StockListComponent({
  stocks,
  setStocks,
  prices,
  setPrices,
  authToken,
  setAuthToken,
}) {
  const [inputData, setInputData] = useState({ symbol: "", buyPrice: "", quantity: "" });
  const [isLoading, setIsLoading] = useState(false);

  async function getLivePrice(symbol) {
    const response = await fetch(`http://localhost:8080/api/prices/${symbol}`);
    const data = await response.json();
    return { price: data.currentPrice, percent: data.percentChange };
  }

  async function fetchPrices(stockListToFetch) {
    const targetList = stockListToFetch || stocks;
    setIsLoading(true);
    const updatedPrices = { ...prices };
    try {
      for (const s of targetList) {
        const price = await getLivePrice(s.symbol);
        updatedPrices[s.symbol] = price;
      }
      setPrices(updatedPrices);
    } catch (error) {
      console.error("Refresh failed", error);
    } finally {
      setIsLoading(false);
    }
  }

  async function addStock() {
    const cleanInputVal = inputData.symbol.trim().toUpperCase();
    if (cleanInputVal.length === 0) {
      alert("Please enter a symbol");
      return;
    }
    if (stocks.some((s) => s.symbol === cleanInputVal)) {
      alert("This stock is already in your watchlist!");
      return;
    }

    setIsLoading(true);
    const validationData = await getLivePrice(cleanInputVal);

    if (validationData.price === 0) {
      alert(`Symbol "${cleanInputVal}" not found. Please check the ticker.`);
      setInputData({ symbol: "", buyPrice: "", quantity: "" });
      setIsLoading(false);
      return;
    }

    const response = await fetch("http://localhost:8080/api/stocks", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${authToken}`,
      },
      body: JSON.stringify({
        symbol: cleanInputVal,
        buyPrice: Number(inputData.buyPrice),
        quantity: Number(inputData.quantity),
      }),
    });

    const jsonResponse = await response.json();
    setStocks((stocks) => [...stocks, jsonResponse.data]);
    setInputData({ symbol: "", buyPrice: "", quantity: "" });
    setIsLoading(false);
    setPrices((prevPrices) => ({
      ...prevPrices,
      [jsonResponse.data.symbol]: validationData,
    }));
  }

  function updateInputData(e) {
    const { name, value } = e.target;
    setInputData((prev) => ({ ...prev, [name]: value }));
  }

  async function deleteStock(id) {
    await fetch(`http://localhost:8080/api/stocks/${id}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${authToken}` },
    });
    setStocks((stocks) => stocks.filter((s) => s.id !== id));
  }

  function logout() {
    localStorage.removeItem("token");
    setAuthToken("");
    setStocks([]);
    setPrices({});
  }

  useEffect(() => {
    async function getInitialData() {
      const response = await fetch("http://localhost:8080/api/stocks?sortBy=id&direction=desc", {
        headers: { Authorization: `Bearer ${authToken}` },
      });
      if (!response.ok) {
        console.error("Initial stock fetch failed", response.status);
        return;
      }
      const jsonResponse = await response.json();
      const stockList = jsonResponse.data.content;
      setStocks(stockList);
      await fetchPrices(stockList);
    }
    getInitialData();
  }, [authToken]);

  // Derived stats
  const totalPortfolioValue = stocks.reduce((acc, s) => {
    const currentPrice = prices[s.symbol]?.price || 0;
    return acc + currentPrice * s.quantity;
  }, 0);

  const totalInvestment = stocks.reduce((acc, s) => acc + s.buyPrice * s.quantity, 0);
  const totalProfit = totalPortfolioValue - totalInvestment;
  const totalProfitPct =
    totalInvestment > 0 ? ((totalProfit / totalInvestment) * 100).toFixed(2) : 0;
  const winCount = stocks.filter((s) => {
    const p = prices[s.symbol]?.price;
    return p && p >= s.buyPrice;
  }).length;

  return (
    <div className="dashboard-wrapper">
      {/* TOP BAR */}
      <header className="topbar">
        <div className="topbar-logo">
          <div className="logo-dot" style={{ marginRight: "8px" }} />
          STOCKPULSE
        </div>
        <div className="topbar-right">
          <div className="market-status">
            <div className="status-dot" />
            MARKETS OPEN
          </div>
          <span style={{ color: "var(--border)", fontSize: "0.8rem" }}>|</span>
          <span
            style={{
              fontSize: "0.68rem",
              letterSpacing: "0.05em",
              color: "var(--text-muted)",
            }}
          >
            NYSE · NASDAQ
          </span>
          <button className="btn-logout" onClick={logout}>
            LOGOUT
          </button>
        </div>
      </header>

      {/* MAIN DASHBOARD */}
      <main className="dashboard-main">
        {/* Page header */}
        <div
          style={{
            marginBottom: "32px",
            display: "flex",
            alignItems: "flex-end",
            justifyContent: "space-between",
          }}
        >
          <div>
            <div
              style={{
                fontSize: "0.65rem",
                letterSpacing: "0.12em",
                textTransform: "uppercase",
                color: "var(--text-muted)",
                marginBottom: "8px",
              }}
            >
              PORTFOLIO DASHBOARD
            </div>
            <h1
              style={{
                fontFamily: "var(--font-display)",
                fontSize: "2.2rem",
                fontWeight: 800,
                letterSpacing: "-0.02em",
                lineHeight: 1,
              }}
            >
              Active Holdings
            </h1>
          </div>
          <div
            style={{
              fontSize: "0.7rem",
              color: "var(--text-muted)",
              letterSpacing: "0.05em",
              textAlign: "right",
            }}
          >
            <div>
              {new Date().toLocaleDateString("en-US", {
                weekday: "long",
                year: "numeric",
                month: "long",
                day: "numeric",
              })}
            </div>
            <div style={{ color: "var(--accent)", marginTop: "4px" }}>
              {stocks.length} POSITION{stocks.length !== 1 ? "S" : ""}
            </div>
          </div>
        </div>

        {/* STATS ROW */}
        <div className="stats-row">
          <div className="stat-card">
            <div className="stat-label">Total Portfolio Value</div>
            <div className="stat-value neutral">${totalPortfolioValue.toFixed(2)}</div>
            <div className="stat-sub">Across {stocks.length} holdings</div>
          </div>
          <div className="stat-card">
            <div className="stat-label">Total Investment</div>
            <div className="stat-value neutral">${totalInvestment.toFixed(2)}</div>
            <div className="stat-sub">Cost basis</div>
          </div>
          <div className="stat-card">
            <div className="stat-label">Profit / Loss</div>
            <div className={`stat-value ${totalProfit >= 0 ? "positive" : "negative"}`}>
              {totalProfit >= 0 ? "+" : ""}${totalProfit.toFixed(2)}
            </div>
            <div className="stat-sub">
              {totalProfit >= 0 ? "▲" : "▼"} {Math.abs(totalProfitPct)}% overall
            </div>
          </div>
          <div className="stat-card">
            <div className="stat-label">Winners</div>
            <div className={`stat-value ${winCount > 0 ? "positive" : "neutral"}`}>
              {winCount} / {stocks.length}
            </div>
            <div className="stat-sub">In profit</div>
          </div>
        </div>

        {/* HOLDINGS TABLE */}
        <div className="holdings-card">
          <div className="holdings-header">
            <div>
              <span className="holdings-title">Positions</span>
              <span className="holdings-count">[{stocks.length} ACTIVE]</span>
            </div>
            <button
              className="btn-refresh"
              onClick={() => fetchPrices(stocks)}
              disabled={isLoading}
            >
              {isLoading ? (
                <>
                  <span className="spin">↻</span> SYNCING...
                </>
              ) : (
                <>↻ REFRESH PRICES</>
              )}
            </button>
          </div>

          <div style={{ overflowX: "auto" }}>
            <table className="holdings-table">
              <thead>
                <tr>
                  <th style={{ paddingLeft: "28px" }}>Symbol</th>
                  <th className="center">Qty</th>
                  <th className="right">Avg. Cost</th>
                  <th className="right">Market Price</th>
                  <th className="right">Total Value</th>
                  <th className="right">P / L</th>
                  <th className="center" style={{ paddingRight: "20px" }}>
                    Action
                  </th>
                </tr>
              </thead>
              <tbody>
                {stocks.length === 0 ? (
                  <tr className="placeholder-row">
                    <td colSpan="7">
                      <span className="placeholder-icon">📈</span>
                      No positions tracked yet. Add your first holding below.
                    </td>
                  </tr>
                ) : (
                  stocks.map((s) => {
                    const priceObj = prices[s.symbol];
                    const currentVal = priceObj ? priceObj.price * s.quantity : 0;
                    const costBasis = s.buyPrice * s.quantity;
                    const pnl = currentVal - costBasis;
                    const isProfit = priceObj ? priceObj.price >= s.buyPrice : true;

                    return (
                      <tr key={s.id}>
                        <td style={{ paddingLeft: "28px" }}>
                          <div className="td-symbol">{s.symbol}</div>
                          <div className="td-type">Equity · US Market</div>
                        </td>
                        <td className="td-center">{s.quantity}</td>
                        <td className="td-right td-muted">${s.buyPrice.toFixed(2)}</td>
                        <td className="td-right">
                          {priceObj ? (
                            <>
                              <div className="price-main">${priceObj.price.toFixed(2)}</div>
                              <div
                                className={`price-change ${priceObj.percent >= 0 ? "up" : "down"}`}
                              >
                                {priceObj.percent >= 0 ? "▲" : "▼"}{" "}
                                {Math.abs(priceObj.percent).toFixed(2)}%
                              </div>
                            </>
                          ) : (
                            <span className="skeleton" style={{ width: "60px", height: "14px" }} />
                          )}
                        </td>
                        <td className="td-right">
                          {priceObj ? (
                            <span className={isProfit ? "value-positive" : "value-negative"}>
                              ${currentVal.toFixed(2)}
                            </span>
                          ) : (
                            "—"
                          )}
                        </td>
                        <td className="td-right">
                          {priceObj ? (
                            <span className={pnl >= 0 ? "value-positive" : "value-negative"}>
                              {pnl >= 0 ? "+" : ""}${pnl.toFixed(2)}
                            </span>
                          ) : (
                            "—"
                          )}
                        </td>
                        <td className="td-center" style={{ paddingRight: "20px" }}>
                          <button className="btn-delete" onClick={() => deleteStock(s.id)}>
                            ✕ REMOVE
                          </button>
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* ADD STOCK PANEL */}
        <div className="add-panel" style={{ marginTop: "16px" }}>
          <div className="add-panel-header">
            <span style={{ color: "var(--accent)" }}>+</span>
            ADD NEW POSITION
          </div>
          <div className="add-panel-body">
            <div className="add-input-wrap">
              <label className="add-input-label">Ticker Symbol</label>
              <input
                className="add-input"
                type="text"
                name="symbol"
                placeholder="e.g. AAPL"
                value={inputData.symbol}
                onChange={updateInputData}
                list="symbolList"
                style={{ textTransform: "uppercase" }}
              />
              <datalist id="symbolList">
                {TOP_SYMBOLS.map((s) => (
                  <option value={s} key={s} />
                ))}
              </datalist>
            </div>

            <div className="add-input-wrap">
              <label className="add-input-label">Buy Price (USD)</label>
              <input
                className="add-input"
                type="number"
                name="buyPrice"
                placeholder="0.00"
                value={inputData.buyPrice}
                onChange={updateInputData}
              />
            </div>

            <div className="add-input-wrap">
              <label className="add-input-label">Quantity</label>
              <input
                className="add-input"
                type="number"
                name="quantity"
                placeholder="1"
                value={inputData.quantity}
                onChange={updateInputData}
              />
            </div>

            <button className="btn-add" onClick={addStock} disabled={isLoading}>
              {isLoading ? (
                <>
                  <span className="spin">↻</span>
                </>
              ) : (
                "+ ADD"
              )}
            </button>
          </div>
        </div>
      </main>

      {/* FOOTER */}
      <footer className="dashboard-footer">
        <span>© 2026 STOCKPULSE PORTFOLIO MANAGER</span>
        <span>DATA BY FINNHUB API</span>
      </footer>
    </div>
  );
}
