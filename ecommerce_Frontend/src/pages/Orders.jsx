import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getOrders } from "../api/orderApi";
import Loading from "../components/Loading";

export default function Orders() {
  const [orders, setOrders] = useState(null);
  useEffect(() => { getOrders().then(r => setOrders(r.data || [])).catch(() => setOrders([])); }, []);

  if (!orders) return <main className="container section"><Loading /></main>;

  return (
    <main className="container section">
      <div className="eyebrow">ACCOUNT</div><h1>My orders</h1>
      {!orders.length ? <div className="state-card"><h3>No orders yet</h3><Link to="/products" className="btn btn-primary">Start shopping</Link></div> :
      <div className="order-list">{orders.map(o => (
        <Link to={`/orders/${o.id}`} className="order-card" key={o.id}>
          <div><strong>Order #{o.id}</strong><span>{new Date(o.createdAt).toLocaleString()}</span></div>
          <div><span className={`status ${String(o.status).toLowerCase()}`}>{o.status}</span><strong>₹{Number(o.totalAmount).toLocaleString("en-IN")}</strong></div>
        </Link>
      ))}</div>}
    </main>
  );
}
