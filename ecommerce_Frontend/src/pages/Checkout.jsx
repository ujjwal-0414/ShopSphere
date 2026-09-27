import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAddresses, createAddress } from "../api/addressApi";
import { placeOrder } from "../api/orderApi";
import { createPayment } from "../api/paymentApi";
import { getCart } from "../api/cartApi";

const blank = { fullName:"", phoneNumber:"", addressLine:"", city:"", state:"", country:"India", postalCode:"", addressType:"HOME", isDefault:false };

export default function Checkout() {
  const navigate = useNavigate();
  const [addresses, setAddresses] = useState([]);
  const [selected, setSelected] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState(blank);
  const [paymentMethod, setPaymentMethod] = useState("UPI");
  const [total, setTotal] = useState(0);
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);

  const load = async () => {
    const [a,c] = await Promise.all([getAddresses(), getCart()]);
    setAddresses(a.data || []);
    setTotal(c.data?.totalPrice || 0);
    const def = (a.data || []).find(x => x.isDefault);
    if (def) setSelected(String(def.id));
  };

  useEffect(() => { load().catch(e => setError(e.response?.data?.message || "Could not load checkout")); }, []);

  const addAddress = async (e) => {
    e.preventDefault();
    try {
      const r = await createAddress(form);
      setAddresses(prev => [...prev, r.data]);
      setSelected(String(r.data.id));
      setShowForm(false);
      setForm(blank);
    } catch(e) { setError(e.response?.data?.message || "Could not create address"); }
  };

  const submit = async () => {
    if (!selected) { setError("Please select or add a delivery address."); return; }
    setBusy(true); setError("");
    try {
      const order = await placeOrder(Number(selected));
      const payment = await createPayment(order.data.id, paymentMethod);
      navigate(`/orders/${order.data.id}`, { state: { payment: payment.data } });
    } catch(e) {
      setError(e.response?.data?.message || "Checkout failed");
    } finally { setBusy(false); }
  };

  return (
    <main className="container section">
      <div className="eyebrow">CHECKOUT</div><h1>Complete your order</h1>
      {error && <div className="error-box">{error}</div>}
      <div className="checkout-grid">
        <section className="panel">
          <div className="panel-head"><h2>Delivery address</h2><button className="btn btn-ghost btn-small" onClick={() => setShowForm(v=>!v)}>{showForm ? "Close" : "Add new"}</button></div>
          {showForm && (
            <form className="address-form" onSubmit={addAddress}>
              <div className="two-col">
                <input required placeholder="Full name" value={form.fullName} onChange={e=>setForm({...form,fullName:e.target.value})}/>
                <input required pattern="[6-9][0-9]{9}" placeholder="Phone number" value={form.phoneNumber} onChange={e=>setForm({...form,phoneNumber:e.target.value})}/>
              </div>
              <input required placeholder="Address line" value={form.addressLine} onChange={e=>setForm({...form,addressLine:e.target.value})}/>
              <div className="two-col"><input required placeholder="City" value={form.city} onChange={e=>setForm({...form,city:e.target.value})}/><input required placeholder="State" value={form.state} onChange={e=>setForm({...form,state:e.target.value})}/></div>
              <div className="two-col"><input required placeholder="Country" value={form.country} onChange={e=>setForm({...form,country:e.target.value})}/><input required pattern="[1-9][0-9]{5}" placeholder="PIN code" value={form.postalCode} onChange={e=>setForm({...form,postalCode:e.target.value})}/></div>
              <select value={form.addressType} onChange={e=>setForm({...form,addressType:e.target.value})}><option>HOME</option><option>OFFICE</option><option>OTHER</option></select>
              <label className="check"><input type="checkbox" checked={form.isDefault} onChange={e=>setForm({...form,isDefault:e.target.checked})}/> Make default</label>
              <button className="btn btn-dark">Save address</button>
            </form>
          )}
          <div className="address-list">
            {addresses.map(a => (
              <label className={`address-card ${selected === String(a.id) ? "selected" : ""}`} key={a.id}>
                <input type="radio" name="address" checked={selected===String(a.id)} onChange={()=>setSelected(String(a.id))}/>
                <div><strong>{a.fullName} {a.isDefault && <small className="badge">DEFAULT</small>}</strong><p>{a.addressLine}, {a.city}, {a.state} - {a.postalCode}</p><small>{a.phoneNumber}</small></div>
              </label>
            ))}
          </div>
        </section>
        <aside className="summary">
          <h3>Payment</h3>
          <label className="payment-option"><input type="radio" name="pay" checked={paymentMethod==="UPI"} onChange={()=>setPaymentMethod("UPI")}/> UPI</label>
          <label className="payment-option"><input type="radio" name="pay" checked={paymentMethod==="CARD"} onChange={()=>setPaymentMethod("CARD")}/> Card</label>
          <label className="payment-option"><input type="radio" name="pay" checked={paymentMethod==="NET_BANKING"} onChange={()=>setPaymentMethod("NET_BANKING")}/> Net Banking</label>
          <hr/><div className="total"><span>Total</span><strong>₹{Number(total).toLocaleString("en-IN")}</strong></div>
          <button className="btn btn-primary full" disabled={busy || !selected} onClick={submit}>{busy ? "Processing..." : "Place order & pay"}</button>
          <small className="muted">Payment is simulated by your backend.</small>
        </aside>
      </div>
    </main>
  );
}
