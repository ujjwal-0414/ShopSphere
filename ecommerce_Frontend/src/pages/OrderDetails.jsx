import { useEffect, useState } from "react";
import { Link, useParams, useLocation } from "react-router-dom";
import { getOrder, cancelOrder } from "../api/orderApi";
import { getPaymentByOrder } from "../api/paymentApi";
import Loading from "../components/Loading";

export default function OrderDetails() {
  const { id } = useParams();
  const location = useLocation();
  const [order, setOrder] = useState(null);
  const [payment, setPayment] = useState(location.state?.payment || null);
  const [error, setError] = useState("");

  const load = async () => {
    const [o,p] = await Promise.all([getOrder(id), getPaymentByOrder(id).catch(()=>({data:null}))]);
    setOrder(o.data); if (p.data) setPayment(p.data);
  };
  useEffect(() => { load().catch(e => setError(e.response?.data?.message || "Could not load order")); }, [id]);

  const cancel = async () => {
    if (!confirm("Cancel this order?")) return;
    try { const r = await cancelOrder(id); setOrder(r.data); } catch(e) { setError(e.response?.data?.message || "Could not cancel order"); }
  };

  if (!order && !error) return <main className="container section"><Loading /></main>;
  if (error) return <main className="container section"><div className="error-box">{error}</div></main>;

  return (
    <main className="container section">
      <Link to="/orders" className="back-link">← My orders</Link>
      <div className="page-title"><div><div className="eyebrow">ORDER #{order.id}</div><h1>Order details</h1></div><span className={`status ${String(order.status).toLowerCase()}`}>{order.status}</span></div>
      <div className="detail-layout">
        <section className="panel">
          <h2>Items</h2>
          {order.items.map(i => <div className="order-item" key={i.id}><div><strong>{i.productName}</strong><small>Qty: {i.quantity} × ₹{Number(i.price).toLocaleString("en-IN")}</small></div><strong>₹{Number(i.subtotal).toLocaleString("en-IN")}</strong></div>)}
          <div className="total line"><span>Total</span><strong>₹{Number(order.totalAmount).toLocaleString("en-IN")}</strong></div>
        </section>
        <aside className="panel">
          <h2>Shipping</h2>
          <p><strong>{order.shippingFullName}</strong><br/>{order.shippingPhoneNumber}<br/>{order.shippingAddressLine}<br/>{order.shippingCity}, {order.shippingState}<br/>{order.shippingCountry} - {order.shippingPostalCode}</p>
          {payment && <><hr/><h2>Payment</h2><p>Status: <strong>{payment.status}</strong><br/>Method: {payment.paymentMethod}<br/>Transaction: {payment.transactionId}</p></>}
          {(order.status === "PENDING" || order.status === "CONFIRMED") && <button className="btn btn-danger" onClick={cancel}>Cancel order</button>}
        </aside>
      </div>
    </main>
  );
}
