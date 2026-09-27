import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { registerUser } from "../api/authApi";

export default function Register() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ firstName:"", lastName:"", email:"", password:"", phoneNumber:"" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true); setError("");
    try {
      await registerUser(form);
      navigate("/login", { state: { message: "Registration successful. Please log in." } });
    } catch (e) {
      setError(e.response?.data?.message || "Registration failed");
    } finally { setLoading(false); }
  };

  const update = (key, value) => setForm({...form, [key]: value});

  return (
    <main className="auth-page">
      <form className="auth-card wide" onSubmit={submit}>
        <div className="eyebrow">JOIN SHOPSPHERE</div>
        <h1>Create account</h1>
        {error && <div className="error-box">{error}</div>}
        <div className="two-col">
          <label>First name<input required value={form.firstName} onChange={e => update("firstName",e.target.value)} /></label>
          <label>Last name<input required value={form.lastName} onChange={e => update("lastName",e.target.value)} /></label>
        </div>
        <label>Email<input type="email" required value={form.email} onChange={e => update("email",e.target.value)} /></label>
        <label>Phone number<input required pattern="[6-9][0-9]{9}" placeholder="10-digit Indian mobile" value={form.phoneNumber} onChange={e => update("phoneNumber",e.target.value)} /></label>
        <label>Password<input type="password" minLength="8" required value={form.password} onChange={e => update("password",e.target.value)} /></label>
        <button className="btn btn-primary full" disabled={loading}>{loading ? "Creating..." : "Create account"}</button>
        <p className="center muted">Already registered? <Link to="/login">Sign in</Link></p>
      </form>
    </main>
  );
}
