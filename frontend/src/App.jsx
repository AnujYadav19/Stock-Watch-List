import { useState } from "react";

import "./App.css";
import StockListComponent from "./components/StockListComponent";
import Login from "./components/Login";
import Home from "./components/Home";

function App() {
  const [stocks, setStocks] = useState([]);
  const [prices, setPrices] = useState({}); //an object where the key is the symbol and the value is its price

  const [userId, setUserId] = useState(localStorage.getItem("userId") || "");

  return (
    <div>
      {userId == "" ? (
        <Home userId={userId} setUserId={setUserId} />
      ) : (
        // <Login userId={userId} setUserId={setUserId} />
        <StockListComponent
          stocks={stocks}
          setStocks={setStocks}
          prices={prices}
          setPrices={setPrices}
          userId={userId}
          setUserId={setUserId}
        />
      )}
    </div>
  );
}

export default App;
