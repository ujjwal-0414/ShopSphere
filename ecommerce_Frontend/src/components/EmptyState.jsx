export default function EmptyState({ title, text, action }) {
  return (
    <div className="state-card">
      <h3>{title}</h3>
      <p>{text}</p>
      {action}
    </div>
  );
}
