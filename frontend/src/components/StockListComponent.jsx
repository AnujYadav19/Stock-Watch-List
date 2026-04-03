import { use, useEffect, useState } from "react";
import { TOP_SYMBOLS } from "../../constants";
export default function StockListComponent({ stocks, setStocks, prices, setPrices }) {
  const [inputVal, setInputVal] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const finnhubToken = import.meta.env.VITE_FINNHUB_TOKEN;

  async function getLivePrice(symbol) {
    const response = await fetch(`http://localhost:8080/api/prices/${symbol}`);
    const data = await response.json();
    return {
      price: data.currentPrice,
      percent: data.percentChange,
    };
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
    const cleanInputVal = inputVal.trim().toUpperCase();
    if (cleanInputVal.length == 0) {
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
      setInputVal("");
      setIsLoading(false);
      return; //exit early, don't save to db
    }

    const response = await fetch("http://localhost:8080/api/stocks", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ symbol: cleanInputVal }),
    });
    const jsonResponse = await response.json();
    setStocks((stocks) => [...stocks, jsonResponse.data]);
    setInputVal("");
    setIsLoading(false);

    const price = validationData;
    setPrices((prevPrices) => ({
      ...prevPrices,
      [jsonResponse.data.symbol]: price,
    }));
  }

  function updateInput(e) {
    setInputVal(e.target.value);
  }

  async function deleteStock(id) {
    await fetch(`http://localhost:8080/api/stocks/${id}`, {
      method: "DELETE",
    });
    setStocks((stocks) => stocks.filter((s) => s.id != id));
  }

  useEffect(() => {
    async function getInitialData() {
      const response = await fetch("http://localhost:8080/api/stocks?sortBy=id&direction=desc");
      const jsonResponse = await response.json();
      const stockList = jsonResponse.data.content;
      setStocks(stockList);
      await fetchPrices(stockList);
    }
    getInitialData();
  }, []);

  return (
    <div className="container mt-5" style={{ maxWidth: "600px" }}>
      <div className="card shadow">
        <div className="card-header bg-dark text-white text-center d-flex justify-content-between align-items-center">
          <h3 className="mb-0">My Watchlist</h3>
          <button
            className="btn btn-sm btn-outline-light"
            onClick={() => fetchPrices(stocks)}
            disabled={isLoading}
          >
            {isLoading ? "Updating..." : "Refresh Prices"}
          </button>
        </div>

        <div className="card-body">
          <table className="table table-hover align-middle">
            <thead className="table-light">
              <tr>
                <th>Symbol</th>
                <th className="text-end">Price</th>
                <th className="text-center">Action</th>
              </tr>
            </thead>
            <tbody>
              {stocks.length === 0 && (
                <tr>
                  <td colSpan="3" className="text-center text-muted py-4">
                    Your watchlist is empty. Add a symbol below!
                  </td>
                </tr>
              )}
              {stocks.map((s) => (
                <tr key={s.id}>
                  <td className="fw-bold text-primary">{s.symbol}</td>
                  <td className="text-end" style={{ width: "150px" }}>
                    {prices[s.symbol] ? (
                      <div className="d-flex flex-column align-items-end">
                        {/* 1. The Main Price */}
                        <span className="fw-bold">${prices[s.symbol].price.toFixed(2)}</span>

                        {/* 2. The Dynamic Color Badge */}
                        <span
                          className={`badge ${prices[s.symbol].percent >= 0 ? "bg-success" : "bg-danger"}`}
                          style={{ fontSize: "0.75rem", marginTop: "2px" }}
                        >
                          {prices[s.symbol].percent >= 0 ? "▲" : "▼"}
                          {Math.abs(prices[s.symbol].percent).toFixed(2)}%
                        </span>
                      </div>
                    ) : (
                      <span className="text-muted small">Loading...</span>
                    )}
                  </td>
                  <td className="text-center">
                    <button
                      className="btn btn-outline-danger btn-sm"
                      onClick={() => deleteStock(s.id)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {/* Your Input Group below the table */}
          <div className="input-group mt-3">
            <input
              type="text"
              className="form-control text-uppercase"
              placeholder="Enter Symbol..."
              value={inputVal}
              onChange={updateInput}
              list="stockDataList"
            />
            <datalist id="stockDataList">
              {inputVal.length > 0 &&
                TOP_SYMBOLS.map((s) => {
                  return <option value={s} key={s}></option>;
                })}
            </datalist>

            <button className="btn btn-primary" disabled={isLoading} onClick={addStock}>
              {isLoading ? "Adding..." : "Add Stock"}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
