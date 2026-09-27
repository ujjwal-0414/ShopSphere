import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { loginUser } from "../api/authApi";
import { useAuth } from "../context/AuthContext";

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true); setError("");
    try {
      const { data } = await loginUser(form);
      login(data);
      navigate(location.state?.from || "/products");
    } catch (e) {
      setError(e.response?.data?.message || "Login failed");
    } finally { setLoading(false); }
  };

  return (
    <main className="auth-page">
      <form className="auth-card" onSubmit={submit}>
        <div className="eyebrow">WELCOME BACK</div>
        <h1>Sign in</h1>
        <p className="muted">Use the account created by your Spring Boot API.</p>
        {error && <div className="error-box">{error}</div>}
        <label>Email<input type="email" required value={form.email} onChange={e => setForm({...form, email:e.target.value})} /></label>
        <label>Password<input type="password" minLength="8" required value={form.password} onChange={e => setForm({...form, password:e.target.value})} /></label>
        <button className="btn btn-primary full" disabled={loading}>{loading ? "Signing in..." : "Sign in"}</button>
        <p className="center muted">Don't have an account? <Link to="/register">Register</Link></p>
      </form>
    </main>
  );
}
