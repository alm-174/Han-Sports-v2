import axiosClient, { axiosPublic } from "./axiosSetup";

export const settingApi = {
  getAllSettings() {
    return axiosPublic.get("/api/v1/settings");
  },

  getAdminSettings() {
    return axiosClient.get("/api/v1/admin/settings");
  },
  
  updateSiteSettings(settings) {
    return axiosClient.put("/api/v1/admin/settings/site", settings);
  },

  updateBulkSettings(updates) {
    return axiosClient.put("/api/v1/settings/bulk", updates);
  }
};
