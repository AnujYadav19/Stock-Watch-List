import { useEffect, useRef, useState } from "react";
import Login from "./Login";
import Register from "./Register";

const TICKER_DATA = [
  { symbol: "AAPL", price: "198.52", change: "+1.24%" },
  { symbol: "TSLA", price: "248.87", change: "-0.83%" },
  { symbol: "NVDA", price: "875.39", change: "+3.11%" },
  { symbol: "MSFT", price: "415.20", change: "+0.56%" },
  { symbol: "GOOGL", price: "172.44", change: "+1.90%" },
  { symbol: "AMZN", price: "183.75", change: "-0.42%" },
  { symbol: "META", price: "502.18", change: "+2.37%" },
  { symbol: "NFLX", price: "634.50", change: "+0.98%" },
  { symbol: "AAPL", price: "198.52", change: "+1.24%" },
  { symbol: "TSLA", price: "248.87", change: "-0.83%" },
  { symbol: "NVDA", price: "875.39", change: "+3.11%" },
  { symbol: "MSFT", price: "415.20", change: "+0.56%" },
  { symbol: "GOOGL", price: "172.44", change: "+1.90%" },
  { symbol: "AMZN", price: "183.75", change: "-0.42%" },
  { symbol: "META", price: "502.18", change: "+2.37%" },
  { symbol: "NFLX", price: "634.50", change: "+0.98%" },
];

const FEATURES = [
  {
    num: "01",
    icon: "⚡",
    title: "Live Market Data",
    desc: "Real-time price feeds via professional-grade APIs. See your holdings update the moment markets move.",
  },
  {
    num: "02",
    icon: "📊",
    title: "P&L Analytics",
    desc: "Instantly know your profit or loss per holding. Weighted average cost tracking built in.",
  },
  {
    num: "03",
    icon: "🔒",
    title: "Secure & Private",
    desc: "Your portfolio is yours alone. Token-based auth, private user accounts, zero data sharing.",
  },
];

const TERMINAL_STOCKS = [
  { symbol: "AAPL", name: "Apple Inc.", price: "$198.52", change: "+1.24%", up: true },
  { symbol: "NVDA", name: "NVIDIA Corp.", price: "$875.39", change: "+3.11%", up: true },
  { symbol: "TSLA", name: "Tesla Inc.", price: "$248.87", change: "-0.83%", up: false },
  { symbol: "MSFT", name: "Microsoft", price: "$415.20", change: "+0.56%", up: true },
];

export default function Home({ setAuthToken }) {
  const [authMode, setAuthMode] = useState("none");
  const authCardRef = useRef(null);

  useEffect(() => {
    if (authMode === "none") return;
    if (!window.matchMedia("(max-width: 1120px)").matches) return;

    requestAnimationFrame(() => {
      authCardRef.current?.scrollIntoView({
        behavior: "smooth",
        block: "start",
      });
    });
  }, [authMode]);

  function openAuth(mode) {
    setAuthMode(mode);
  }

  return (
    <div className="home-wrapper">
      {/* HERO SECTION */}
      <section className="hero-section">
        <div className="hero-bg" />
        <div className="hero-grid" />

        {/* Scrolling Ticker */}
        <div className="ticker-wrapper">
          <div className="ticker-track">
            {TICKER_DATA.map((t, i) => (
              <span key={i} className="ticker-item">
                <strong>{t.symbol}</strong>
                <span>{t.price}</span>
                <span className={t.change.startsWith("+") ? "up" : "down"}>{t.change}</span>
              </span>
            ))}
          </div>
        </div>

        {/* Nav */}
        <nav className="hero-nav">
          <div className="nav-inner">
            <div className="logo" onClick={() => setAuthMode("none")}>
              <div className="logo-dot" />
              STOCKPULSE
            </div>
            <div className="nav-actions">
              <button className="btn-ghost" onClick={() => openAuth("login")}>
                LOGIN
              </button>
              <button className="btn-primary-hero" onClick={() => openAuth("register")}>
                GET STARTED →
              </button>
            </div>
          </div>
        </nav>

        {/* Hero Content */}
        <div style={{ flex: 1, display: "flex", alignItems: "center" }}>
          <div className="hero-content">
            <div className="hero-left">
              <div className="hero-tag">
                <span>●</span> LIVE MARKET TRACKER
              </div>
              <h1 className="hero-title">
                <span className="dim-line">TRADE</span>
                <span className="accent-line">SMARTER.</span>
                <span className="dim-line" style={{ color: "var(--text-secondary)" }}>
                  ALWAYS.
                </span>
              </h1>
              <p className="hero-subtitle">
                Real-time portfolio tracking with live P&L, average cost analytics, and a dashboard
                that actually tells you what's happening to your money.
              </p>
              <div className="hero-cta-group">
                <button className="btn-cta" onClick={() => openAuth("register")}>
                  START TRACKING →
                </button>
                <button
                  className="btn-cta-outline"
                  onClick={() =>
                    document.getElementById("features")?.scrollIntoView({ behavior: "smooth" })
                  }
                >
                  HOW IT WORKS
                </button>
              </div>
            </div>

            {/* Right side: either auth card or terminal widget */}
            {authMode === "none" ? (
              <div className="hero-terminal">
                <div className="terminal-header">
                  <div className="terminal-dot td-red" />
                  <div className="terminal-dot td-yellow" />
                  <div className="terminal-dot td-green" />
                  <span className="terminal-title">PORTFOLIO — LIVE VIEW</span>
                </div>
                <div className="terminal-body">
                  {TERMINAL_STOCKS.map((s) => (
                    <div className="t-row" key={s.symbol}>
                      <div>
                        <div className="t-symbol">{s.symbol}</div>
                        <div className="t-name">{s.name}</div>
                      </div>
                      <div className="t-price">
                        <div className="t-price-val">{s.price}</div>
                        <div className={s.up ? "t-change-up" : "t-change-down"}>
                          {s.up ? "▲" : "▼"} {s.change}
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
                <div className="terminal-footer">
                  <span>● MARKETS OPEN</span>
                  <span>NYSE · NASDAQ</span>
                </div>
              </div>
            ) : (
              <div className="auth-card" ref={authCardRef}>
                <div className="auth-header">
                  <div>
                    <div className="auth-title">
                      {authMode === "login" ? "Welcome back." : "Join the pulse."}
                    </div>
                    <div className="auth-sub">
                      {authMode === "login"
                        ? "Sign in to your portfolio."
                        : "Create your free account."}
                    </div>
                  </div>
                  <button className="auth-close" onClick={() => setAuthMode("none")}>
                    ✕
                  </button>
                </div>

                {authMode === "login" ? (
                  <Login setAuthToken={setAuthToken} />
                ) : (
                  <Register setAuthToken={setAuthToken} />
                )}

                <div className="auth-switch">
                  {authMode === "login" ? "New here?" : "Already a member?"}
                  <button
                    className="auth-switch-btn"
                    onClick={() => openAuth(authMode === "login" ? "register" : "login")}
                  >
                    {authMode === "login" ? "Create account →" : "Sign in →"}
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      </section>

      {/* FEATURES SECTION */}
      <section id="features" className="features-section">
        <div className="section-label">PLATFORM FEATURES</div>
        <h2 className="section-title">
          Everything you need.
          <br />
          Nothing you don't.
        </h2>
        <p className="section-sub">Built for traders who want clarity, not noise.</p>
        <div className="features-grid">
          {FEATURES.map((f) => (
            <div className="feature-card" key={f.num}>
              <div className="feature-num">{f.num}</div>
              <span className="feature-icon">{f.icon}</span>
              <div className="feature-title">{f.title}</div>
              <div className="feature-desc">{f.desc}</div>
            </div>
          ))}
        </div>
      </section>

      {/* FOOTER */}
      <footer className="site-footer">
        <div className="footer-logo">
          <span style={{ color: "var(--accent)" }}>●</span> STOCKPULSE
        </div>
        <div className="footer-text">
          © 2026 StockPulse Portfolio Manager · Built with React & Spring Boot · Data by Finnhub API
        </div>
      </footer>
    </div>
  );
}
