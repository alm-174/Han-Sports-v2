import { create } from "zustand";
import { settingApi } from "../api/settingApi";

export const useSettingStore = create((set, get) => ({
  settings: null,
  loading: false,
  error: null,
  fetchSettings: async () => {
    if (get().settings) return; // already fetched
    set({ loading: true, error: null });
    try {
      const res = await settingApi.getAllSettings();
      set({ settings: res.data?.data || res.data || {}, error: null });
    } catch (e) {
      console.error("Failed to fetch settings", e);
      set({ error: e?.message || "Failed to fetch settings" });
    } finally {
      set({ loading: false });
    }
  },
  
  getSetting: (key, defaultValue = null) => {
    const s = get().settings;
    if (!s || s[key] === undefined) return defaultValue;
    try {
      // try to parse JSON if it's an array/object
      if (typeof s[key] === "string" && (s[key].startsWith("[") || s[key].startsWith("{"))) {
        return JSON.parse(s[key]);
      }
      return s[key];
    } catch {
      return s[key];
    }
  },
  
  // call this after admin updates settings
  refreshSettings: async () => {
    try {
      const res = await settingApi.getAllSettings();
      set({ settings: res.data?.data || res.data || {}, error: null });
    } catch (e) {
      console.error(e);
      set({ error: e?.message || "Failed to refresh settings" });
    }
  }
}));
