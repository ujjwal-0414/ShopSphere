import { createContext, useContext, useMemo, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem("ecommerce_user") || "null");
    } catch {
      return null;
    }
  });

  const token = localStorage.getItem("ecommerce_token");

  const login = (authResponse) => {
    localStorage.setItem("ecommerce_token", authResponse.token);
    localStorage.setItem("ecommerce_user", JSON.stringify(authResponse.user));
    setUser(authResponse.user);
  };

  const logout = () => {
    localStorage.removeItem("ecommerce_token");
    localStorage.removeItem("ecommerce_user");
    setUser(null);
  };

  const value = useMemo(
    () => ({ user, token, isAuthenticated: !!token && !!user, login, logout }),
    [user, token]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
