import { Component } from "react";

export default class ErrorBoundary extends Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error };
  }

  componentDidCatch(error) {
    console.error("ShopSphere UI error:", error);
  }

  render() {
    if (!this.state.hasError) return this.props.children;

    return (
      <main className="container section">
        <div className="state-card">
          <div className="empty-cart-icon">⚠️</div>
          <h3>This page could not be displayed</h3>
          <p>Refresh the page. If the problem continues, open the browser console for the exact error.</p>
          <button
            className="btn btn-primary"
            onClick={() => window.location.reload()}
          >
            Reload
          </button>
        </div>
      </main>
    );
  }
}
