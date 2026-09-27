import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";
import { getCart } from "../api/cartApi";
import { useAuth } from "./AuthContext";

const CartContext = createContext(null);

export function CartProvider({ children }) {
  const { isAuthenticated } = useAuth();
  const [cart, setCart] = useState({ items: [], totalPrice: 0 });

  const refreshCart = useCallback(async () => {
    if (!isAuthenticated) {
      setCart({ items: [], totalPrice: 0 });
      return;
    }
    try {
      const { data } = await getCart();
      setCart(data || { items: [], totalPrice: 0 });
    } catch {
      // The protected cart page will show the actual API error.
    }
  }, [isAuthenticated]);

  useEffect(() => {
    refreshCart();
  }, [refreshCart]);

  const itemCount = (cart.items || []).reduce((sum, item) => sum + (item.quantity || 0), 0);

  const value = useMemo(
    () => ({ cart, setCart, refreshCart, itemCount }),
    [cart, refreshCart, itemCount]
  );

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}

export function useCart() {
  return useContext(CartContext);
}
