import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "./assets/vite.svg";
import heroImg from "./assets/hero.png";
import "./App.css";
import StockList from "./components/StockListComponent";
import StockListComponent from "./components/StockListComponent";

function App() {
  const [stocks, setStocks] = useState([]);
  const [prices, setPrices] = useState({}); //an object where the key is the symbol and the value is its price

  return (
    <div>
      <StockListComponent
        stocks={stocks}
        setStocks={setStocks}
        prices={prices}
        setPrices={setPrices}
      />
    </div>
  );
}

export default App;
