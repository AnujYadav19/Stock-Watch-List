import { useState } from "react";

import "./App.css";
import StockListComponent from "./components/StockListComponent";
import Login from "./components/Login";
import Home from "./components/Home";

function App() {
  const [stocks, setStocks] = useState([]);
  const [prices, setPrices] = useState({}); //an object where the key is the symbol and the value is its price

  const [authToken, setAuthToken] = useState(localStorage.getItem("token") || "");

  return (
    <div>
      {authToken == "" ? (
        <Home authToken={authToken} setAuthToken={setAuthToken} />
      ) : (
        <StockListComponent
          stocks={stocks}
          setStocks={setStocks}
          prices={prices}
          setPrices={setPrices}
          authToken={authToken}
          setAuthToken={setAuthToken}
        />
      )}
    </div>
  );
}

export default App;
