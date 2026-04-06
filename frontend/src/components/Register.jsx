import { useState } from "react";

export default function Register({ setUserId }) {
  const [formData, setFormData] = useState({ username: "", email: "", password: "" });
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
      const response = await fetch("http://localhost:8080/api/user/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      const jsonResponse = await response.json();

      if (jsonResponse.success) {
        setUserId(jsonResponse.data.id);
        localStorage.setItem("userId", jsonResponse.data.id);
      } else {
        setError(jsonResponse.message || "Registration failed. Try again.");
      }
    } catch (err) {
      setError("Server unreachable. Please check if Spring Boot is running.");
      console.error("Register Error:", err);
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      {error && <div className="form-error">{error}</div>}

      <div className="form-group">
        <label className="form-label" htmlFor="reg-username">
          Username
        </label>
        <input
          className="form-input"
          type="text"
          name="username"
          id="reg-username"
          placeholder="choose_a_username"
          value={formData.username}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label className="form-label" htmlFor="reg-email">
          Email
        </label>
        <input
          className="form-input"
          type="email"
          name="email"
          id="reg-email"
          placeholder="name@example.com"
          value={formData.email}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label className="form-label" htmlFor="reg-password">
          Password
        </label>
        <input
          className="form-input"
          type="password"
          name="password"
          id="reg-password"
          placeholder="Min. 8 characters"
          value={formData.password}
          onChange={handleChange}
          required
        />
      </div>

      <button className="btn-submit" type="submit" disabled={isLoading}>
        {isLoading ? (
          <>
            <span className="spin">↻</span> CREATING ACCOUNT...
          </>
        ) : (
          "CREATE ACCOUNT →"
        )}
      </button>
    </form>
  );
}
