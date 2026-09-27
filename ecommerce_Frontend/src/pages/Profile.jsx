import { useAuth } from "../context/AuthContext";

export default function Profile() {
  const { user } = useAuth();
  return (
    <main className="container section">
      <div className="eyebrow">ACCOUNT</div><h1>Profile</h1>
      <div className="profile-card">
        <div className="avatar">{user?.firstName?.[0]}{user?.lastName?.[0]}</div>
        <h2>{user?.firstName} {user?.lastName}</h2>
        <p>{user?.email}</p>
        <p>{user?.phoneNumber}</p>
        <span className="badge">{user?.role}</span>
      </div>
    </main>
  );
}
