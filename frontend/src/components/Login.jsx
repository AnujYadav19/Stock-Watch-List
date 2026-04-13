import { useState } from "react";

export default function Login({ setAuthToken }) {
  const [formData, setFormData] = useState({ username: "", password: "" });
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleChange = (evt) => {
    const { name, value } = evt.target;
    setFormData({ ...formData, [name]: value });
  };

  async function handleSubmit(evt) {
    evt.preventDefault();
    setError("");
    setIsLoading(true);

    try {
      const response = await fetch("http://localhost:8080/api/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      const jsonResponse = await response.json();

      if (jsonResponse.success) {
        setAuthToken(jsonResponse.data.accessToken);
        localStorage.setItem("token", jsonResponse.data.accessToken);
      } else {
        setError(jsonResponse.message || "Invalid credentials.");
      }
    } catch (err) {
      setError("Connection failed. Is the server running?");
      console.error("Login Error:", err);
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}

      <div className="form-group">
        <label className="form-label" htmlFor="login-username">
          Username
        </label>
        <input
          className="form-input"
          type="text"
          name="username"
          id="login-username"
          placeholder="your_username"
          value={formData.username}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label className="form-label" htmlFor="login-password">
          Password
        </label>
        <input
          className="form-input"
          type="password"
          name="password"
          id="login-password"
          placeholder="••••••••"
          value={formData.password}
          onChange={handleChange}
          required
        />
      </div>

      <button className="btn-submit" type="submit" disabled={isLoading}>
        {isLoading ? (
          <>
            <span className="spin">↻</span> VERIFYING...
          </>
        ) : (
          "SIGN IN →"
        )}
      </button>
    </form>
  );
}
