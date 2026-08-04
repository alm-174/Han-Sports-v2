import { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { orderApi } from "../../api/orderApi";
import { ghnApi } from "../../api/ghnApi";
import { useCartStore } from "../../store/useCartStore";
import { useAuthStore } from "../../store/useAuthStore";
import { useSettingStore } from "../../store/useSettingStore";
import { getImageUrl, formatVND, getFirstImage } from "../../utils/constants";

export default function CheckoutPage() {
  const navigate = useNavigate();
  const { user } = useAuthStore();
  const { cartItems, getTotal, selectedIds, removeSelectedItems } = useCartStore();
  const { getSetting } = useSettingStore();
  const [submitting, setSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);
  const [form, setForm] = useState({
    receiverName: user?.fullName || "",
    receiverPhone: user?.phone || "",
    receiverProvince: "",
    receiverDistrict: "",
    receiverWard: "",
    receiverAddress: user?.address || "",
    note: "",
    paymentMethod: "COD",
  });
  const [provinces, setProvinces] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [wards, setWards] = useState([]);
  const [feeInfo, setFeeInfo] = useState(null);
  const [feeLoading, setFeeLoading] = useState(false);
  const [feeError, setFeeError] = useState(null);
  const districtRequestRef = useRef(0);
  const wardRequestRef = useRef(0);
  const feeRequestRef = useRef(0);

  const GHN_FROM_DISTRICT_ID = Number(import.meta.env.VITE_GHN_FROM_DISTRICT_ID || 0);
  const GHN_FROM_WARD_CODE = import.meta.env.VITE_GHN_FROM_WARD_CODE || "";

  const normalizeProvinceData = (data = []) =>
    (Array.isArray(data) ? data : []).map((item) => ({
      provinceId: item?.provinceId ?? item?.ProvinceID ?? item?.ProvinceId ?? item?.id ?? item?.provinceID,
      provinceName: item?.provinceName ?? item?.ProvinceName ?? item?.name ?? "",
    }));

  const normalizeDistrictData = (data = []) =>
    (Array.isArray(data) ? data : []).map((item) => ({
      districtId: item?.districtId ?? item?.DistrictID ?? item?.DistrictId ?? item?.id ?? item?.districtID,
      districtName: item?.districtName ?? item?.DistrictName ?? item?.name ?? "",
      provinceId: item?.provinceId ?? item?.ProvinceID ?? item?.ProvinceId ?? item?.provinceID ?? null,
    }));

  const normalizeWardData = (data = []) =>
    (Array.isArray(data) ? data : []).map((item) => ({
      wardCode: item?.wardCode ?? item?.WardCode ?? item?.code ?? item?.id ?? item?.wardID,
      wardName: item?.wardName ?? item?.WardName ?? item?.name ?? "",
      districtId: item?.districtId ?? item?.DistrictID ?? item?.DistrictId ?? item?.districtID ?? null,
    }));

  // #region agent log
  useEffect(() => {
    fetch('http://127.0.0.1:7583/ingest/b7b6a8d6-8ef5-4ebe-ba7d-aae6265a00d7',{method:'POST',headers:{'Content-Type':'application/json','X-Debug-Session-Id':'c76d45'},body:JSON.stringify({sessionId:'c76d45',location:'CheckoutPage.jsx:mount',message:'GHN env config',data:{fromDistrictId:GHN_FROM_DISTRICT_ID,fromWardCode:GHN_FROM_WARD_CODE,apiUrl:import.meta.env.VITE_API_URL||'(empty)'},timestamp:Date.now(),hypothesisId:'A'})}).catch(()=>{});
  }, []);
  // #endregion

  useEffect(() => {
    if (!user) { navigate("/login"); return; }
    if (cartItems.length === 0) { navigate("/cart"); return; }
  }, [user, cartItems]);

  useEffect(() => {
    ghnApi.getProvinces()
      .then((res) => {
        const data = normalizeProvinceData(res.data?.data || res.data || []);
        // #region agent log
        fetch('http://127.0.0.1:7583/ingest/b7b6a8d6-8ef5-4ebe-ba7d-aae6265a00d7',{method:'POST',headers:{'Content-Type':'application/json','X-Debug-Session-Id':'c76d45'},body:JSON.stringify({sessionId:'c76d45',location:'CheckoutPage.jsx:provinces',message:'provinces loaded',data:{count:Array.isArray(data)?data.length:0,sample:Array.isArray(data)&&data[0]?data[0]:null,status:res.status},timestamp:Date.now(),hypothesisId:'B'})}).catch(()=>{});
        // #endregion
        setProvinces(data);
      })
      .catch((err) => {
        // #region agent log
        fetch('http://127.0.0.1:7583/ingest/b7b6a8d6-8ef5-4ebe-ba7d-aae6265a00d7',{method:'POST',headers:{'Content-Type':'application/json','X-Debug-Session-Id':'c76d45'},body:JSON.stringify({sessionId:'c76d45',location:'CheckoutPage.jsx:provinces',message:'provinces failed',data:{status:err.response?.status,message:err.response?.data?.message||err.message},timestamp:Date.now(),hypothesisId:'B'})}).catch(()=>{});
        // #endregion
        console.error("GHN provinces load failed", err);
        setProvinces([]);
      });
  }, []);

  useEffect(() => {
    if (!form.receiverProvince) {
      setDistricts([]);
      setWards([]);
      setFeeInfo(null);
      setFeeError(null);
      return;
    }

    const requestId = ++districtRequestRef.current;
    wardRequestRef.current += 1;
    feeRequestRef.current += 1;

    setDistricts([]);
    setWards([]);
    setForm((f) => ({ ...f, receiverDistrict: "", receiverWard: "" }));
    setFeeInfo(null);
    setFeeError(null);

    ghnApi.getDistricts(Number(form.receiverProvince))
      .then((res) => {
        if (requestId !== districtRequestRef.current) return;
        const data = normalizeDistrictData(res.data?.data || res.data || []);
        setDistricts(data);
      })
      .catch((err) => {
        if (requestId !== districtRequestRef.current) return;
        console.error("GHN districts load failed", err);
        setDistricts([]);
      });
  }, [form.receiverProvince]);

  useEffect(() => {
    if (!form.receiverProvince || !form.receiverDistrict) {
      setWards([]);
      setFeeInfo(null);
      setFeeError(null);
      return;
    }

    const wardRequestId = ++wardRequestRef.current;

    setWards([]);
    setForm((f) => ({ ...f, receiverWard: "" }));
    setFeeInfo(null);

    ghnApi.getWards(Number(form.receiverDistrict))
      .then((res) => {
        if (wardRequestId !== wardRequestRef.current) return;
        const data = normalizeWardData(res.data?.data || res.data || []);
        setWards(data);
      })
      .catch((err) => {
        if (wardRequestId !== wardRequestRef.current) return;
        console.error("GHN wards load failed", err);
        setWards([]);
      });

    if (!GHN_FROM_DISTRICT_ID || !Number(form.receiverDistrict)) {
      setFeeError("Thiếu thông tin quận/huyện hoặc cấu hình GHN kho gửi");
    }
  }, [form.receiverProvince, form.receiverDistrict]);

  useEffect(() => {
    if (!form.receiverDistrict || !form.receiverWard || !GHN_FROM_DISTRICT_ID || !GHN_FROM_WARD_CODE) {
      setFeeInfo(null);
      return;
    }

    const requestId = ++feeRequestRef.current;

    setFeeLoading(true);
    setFeeError(null);
    ghnApi.getFee({
      toDistrictId: Number(form.receiverDistrict),
      toWardCode: form.receiverWard,
      insuranceValue: Math.round(getTotal()),
      coupon: null,
    })
      .then((res) => {
        if (requestId !== feeRequestRef.current) return;
        const data = res.data?.data || res.data || null;
        // #region agent log
        fetch('http://127.0.0.1:7583/ingest/b7b6a8d6-8ef5-4ebe-ba7d-aae6265a00d7',{method:'POST',headers:{'Content-Type':'application/json','X-Debug-Session-Id':'c76d45'},body:JSON.stringify({sessionId:'c76d45',location:'CheckoutPage.jsx:fee',message:'fee calculated',data:{fee:data},timestamp:Date.now(),hypothesisId:'D'})}).catch(()=>{});
        // #endregion
        setFeeInfo(data);
      })
      .catch((err) => {
        if (requestId !== feeRequestRef.current) return;
        // #region agent log
        fetch('http://127.0.0.1:7583/ingest/b7b6a8d6-8ef5-4ebe-ba7d-aae6265a00d7',{method:'POST',headers:{'Content-Type':'application/json','X-Debug-Session-Id':'c76d45'},body:JSON.stringify({sessionId:'c76d45',location:'CheckoutPage.jsx:fee',message:'fee failed',data:{status:err.response?.status,message:err.response?.data?.message||err.message},timestamp:Date.now(),hypothesisId:'D'})}).catch(()=>{});
        // #endregion
        console.error("GHN fee calculation failed", err);
        setFeeInfo(null);
        setFeeError("Không tính được phí ship GHN");
      })
      .finally(() => {
        if (requestId === feeRequestRef.current) {
          setFeeLoading(false);
        }
      });
  }, [form.receiverDistrict, form.receiverWard]);

  const handleChange = (e) => setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const detailedAddress = form.receiverAddress?.trim();
      const provinceName = provinces.find((p) => String(p.provinceId) === String(form.receiverProvince))?.provinceName || form.receiverProvince;
      const districtName = districts.find((d) => String(d.districtId) === String(form.receiverDistrict))?.districtName || form.receiverDistrict;
      const wardName = wards.find((w) => String(w.wardCode) === String(form.receiverWard))?.wardName || form.receiverWard;
      const composedAddress = [
        detailedAddress,
        wardName && String(wardName).trim(),
        districtName && String(districtName).trim(),
        provinceName && String(provinceName).trim(),
      ].filter(Boolean).join(', ');

      setForm((f) => ({ ...f, receiverAddress: detailedAddress || f.receiverAddress }));

      const orderData = {
        ...form,
        receiverAddress: composedAddress,
        cartDetailIds: selectedIds,
      };

      await orderApi.createOrder(orderData);
      // #region agent log
      fetch('http://127.0.0.1:7583/ingest/b7b6a8d6-8ef5-4ebe-ba7d-aae6265a00d7',{method:'POST',headers:{'Content-Type':'application/json','X-Debug-Session-Id':'c76d45'},body:JSON.stringify({sessionId:'c76d45',location:'CheckoutPage.jsx:submit',message:'order created',data:{cartDetailCount:selectedIds.length,hasFeeInfo:!!feeInfo},timestamp:Date.now(),hypothesisId:'E'})}).catch(()=>{});
      // #endregion
      removeSelectedItems();
      setSuccess(true);
    } catch (err) {
      // #region agent log
      fetch('http://127.0.0.1:7583/ingest/b7b6a8d6-8ef5-4ebe-ba7d-aae6265a00d7',{method:'POST',headers:{'Content-Type':'application/json','X-Debug-Session-Id':'c76d45'},body:JSON.stringify({sessionId:'c76d45',location:'CheckoutPage.jsx:submit',message:'order failed',data:{status:err.response?.status,message:err.response?.data?.message||err.message},timestamp:Date.now(),hypothesisId:'E'})}).catch(()=>{});
      // #endregion
      alert(err.response?.data?.message || "Đặt hàng thất bại, vui lòng thử lại!");
    } finally {
      setSubmitting(false);
    }
  };

  const subtotal = getTotal();
  const freeShipLimit = parseInt(getSetting("FREE_SHIP_LIMIT", "500000"), 10);
  const baseShippingFee = parseInt(getSetting("SHIPPING_FEE", "30000"), 10);
  const ghFee = feeInfo
    ? (feeInfo.serviceFee || 0) + (feeInfo.insuranceFee || 0) + (feeInfo.pickStationFee || 0) + (feeInfo.r2sFee || 0)
    : null;
  const shipping = ghFee !== null ? ghFee : subtotal >= freeShipLimit ? 0 : baseShippingFee;

  if (success) return (
    <div className="min-h-screen bg-surface-soft flex items-center justify-center px-4">
      <div className="card p-10 max-w-md w-full text-center animate-fade-up">
        <div className="w-20 h-20 rounded-full flex items-center justify-center mx-auto mb-6 animate-pulse-green" style={{ background: "linear-gradient(135deg, #16a34a, #0d9488)" }}>
          <span className="material-symbols-outlined text-white" style={{ fontSize: 40, fontVariationSettings: "'FILL' 1" }}>check_circle</span>
        </div>
        <h2 className="text-title font-bold text-text-primary mb-2">Đặt hàng thành công!</h2>
        <p className="text-text-muted mb-8">Cảm ơn bạn đã mua hàng tại HAN SPORTS. Chúng tôi sẽ liên hệ xác nhận đơn trong thời gian sớm nhất.</p>
        <div className="flex flex-col gap-3">
          <button onClick={() => navigate("/orders")} className="btn-primary w-full py-3 rounded-xl">
            <span className="material-symbols-outlined" style={{ fontSize: 18 }}>receipt_long</span>
            Xem đơn hàng của tôi
          </button>
          <button onClick={() => navigate("/")} className="btn-ghost w-full py-3 rounded-xl border border-surface-border">
            Về trang chủ
          </button>
        </div>
      </div>
    </div>
  );

  return (
    <div className="min-h-screen bg-surface-soft py-10">
      <div className="max-w-[1280px] mx-auto px-4 md:px-6">
        <h1 className="text-heading font-bold text-text-primary mb-8 flex items-center gap-3">
          <span className="material-symbols-outlined text-brand-blue" style={{ fontSize: 32 }}>payment</span>
          Thanh toán
        </h1>

        <form onSubmit={handleSubmit}>
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* Form bên trái */}
            <div className="lg:col-span-2 flex flex-col gap-6">
              {/* Thông tin giao hàng */}
              <div className="card p-6">
                <h2 className="text-title font-bold text-text-primary mb-5 flex items-center gap-2">
                  <span className="material-symbols-outlined text-brand-blue" style={{ fontSize: 24 }}>local_shipping</span>
                  Thông tin giao hàng
                </h2>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-semibold text-text-secondary mb-2">Họ và tên *</label>
                    <input name="receiverName" required value={form.receiverName} onChange={handleChange}
                      placeholder="Nguyễn Văn A" className="input-field" />
                  </div>
                  <div>
                    <label className="block text-sm font-semibold text-text-secondary mb-2">Số điện thoại *</label>
                    <input name="receiverPhone" required value={form.receiverPhone} onChange={handleChange}
                      placeholder="090 123 4567" type="tel" className="input-field" />
                  </div>

                  <div>
                    <label className="block text-sm font-semibold text-text-secondary mb-2">Tỉnh/Thành phố *</label>
                    <select
                      name="receiverProvince"
                      value={form.receiverProvince}
                      onChange={(e) => {
                        const value = e.target.value;
                        setForm((prev) => ({ ...prev, receiverProvince: value, receiverDistrict: "", receiverWard: "" }));
                      }}
                      required
                      className="input-field"
                    >
                      <option value="">Chọn Tỉnh/Thành</option>
                      {provinces.map((p) => (
                        <option key={String(p.provinceId)} value={String(p.provinceId)}>
                          {p.provinceName}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div>
                    <label className="block text-sm font-semibold text-text-secondary mb-2">
                      Quận/Huyện *
                      {form.receiverProvince && (
                        <span className="text-xs text-text-muted block mt-1">
                          Tỉnh đã chọn: {provinces.find((p) => String(p.provinceId) === String(form.receiverProvince))?.provinceName || ""} ({form.receiverProvince})
                        </span>
                      )}
                    </label>
                    <select
                      key={form.receiverProvince || "empty-province"}
                      name="receiverDistrict"
                      value={form.receiverDistrict}
                      onChange={(e) => {
                        const value = e.target.value;
                        setForm((prev) => ({ ...prev, receiverDistrict: value, receiverWard: "" }));
                      }}
                      required
                      className="input-field"
                    >
                      <option value="">Chọn Quận/Huyện</option>
                      {districts.map((d) => (
                        <option key={String(d.districtId)} value={String(d.districtId)}>
                          {d.districtName}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div>
                    <label className="block text-sm font-semibold text-text-secondary mb-2">Xã/Phường *</label>
                    <select
                      key={form.receiverDistrict || "empty-district"}
                      name="receiverWard"
                      value={form.receiverWard}
                      onChange={(e) => setForm((prev) => ({ ...prev, receiverWard: e.target.value }))}
                      required
                      className="input-field"
                    >
                      <option value="">Chọn Xã/Phường</option>
                      {wards.map((w) => (
                        <option key={String(w.wardCode)} value={String(w.wardCode)}>{w.wardName}</option>
                      ))}
                    </select>
                  </div>

                  <div className="md:col-span-2">
                    <label className="block text-sm font-semibold text-text-secondary mb-2">Địa chỉ chi tiết *</label>
                    <input
                      name="receiverAddress"
                      required
                      value={form.receiverAddress}
                      onChange={handleChange}
                      placeholder="Số nhà, tên đường, khu vực..."
                      className="input-field"
                    />
                  </div>

                  {!GHN_FROM_DISTRICT_ID && (
                    <div className="md:col-span-2">
                      <p className="text-xs text-text-muted mt-2">Không cấu hình GHN kho gửi. Vui lòng đặt VITE_GHN_FROM_DISTRICT_ID và VITE_GHN_FROM_WARD_CODE.</p>
                    </div>
                  )}

                  <div className="md:col-span-2">
                    <p className="text-sm text-text-muted">Địa chỉ sẽ được ghép từ Địa chỉ chi tiết, Xã/Phường, Quận/Huyện, Tỉnh/Thành.</p>
                  </div>

                  <div className="md:col-span-2">
                    <label className="block text-sm font-semibold text-text-secondary mb-2">Ghi chú (tùy chọn)</label>
                    <textarea name="note" value={form.note} onChange={handleChange} rows={3}
                      placeholder="Ghi chú cho đơn hàng (ví dụ: giao trong giờ hành chính...)"
                      className="input-field resize-none" />
                  </div>
                </div>
              </div>

              {/* Phương thức thanh toán */}
              <div className="card p-6">
                <h2 className="text-title font-bold text-text-primary mb-5 flex items-center gap-2">
                  <span className="material-symbols-outlined text-brand-blue" style={{ fontSize: 24 }}>account_balance_wallet</span>
                  Phương thức thanh toán
                </h2>
                <div className="flex flex-col gap-3">
                  <label className={`flex items-center gap-3 p-4 border-2 rounded-xl cursor-pointer transition-all ${form.paymentMethod === 'COD' ? 'border-brand-blue bg-brand-blue-light' : 'border-surface-border bg-white hover:border-brand-blue/50'}`}>
                    <input type="radio" name="paymentMethod" value="COD" checked={form.paymentMethod === "COD"} onChange={handleChange} className="accent-brand-blue" />
                    <span className="material-symbols-outlined text-brand-blue" style={{ fontSize: 24 }}>payments</span>
                    <div>
                      <p className="font-semibold text-text-primary">Thanh toán khi nhận hàng (COD)</p>
                      <p className="text-xs text-text-muted">Thanh toán tiền mặt khi nhận được hàng</p>
                    </div>
                  </label>

                  {/* VNPay option removed - only COD supported */}
                </div>
              </div>
            </div>

            {/* Order Summary bên phải */}
            <div className="lg:col-span-1">
              <div className="card p-6 sticky top-24">
                <h3 className="text-title font-bold text-text-primary mb-5">Đơn hàng ({selectedIds.length} sản phẩm)</h3>
                <div className="flex flex-col gap-3 mb-5 max-h-60 overflow-y-auto hide-scrollbar">
                  {cartItems.filter(i => selectedIds.includes(i.id)).map((item) => {
                    const p = item.product || item;
                    return (
                      <div key={item.id} className="flex gap-3 items-center">
                        <div className="w-14 h-14 rounded-lg bg-surface-muted flex-shrink-0 overflow-hidden">
                          {getFirstImage(p) && <img src={getImageUrl(getFirstImage(p))} alt={p.name} className="w-full h-full object-contain p-1.5" />}
                        </div>
                        <div className="flex-1 min-w-0">
                          <p className="text-xs font-semibold text-text-primary line-clamp-2">{p.name}</p>
                          <p className="text-xs text-text-muted mt-0.5">x{item.quantity}</p>
                        </div>
                        <span className="text-xs font-bold text-brand-blue flex-shrink-0">{formatVND(p.price * item.quantity)}</span>
                      </div>
                    );
                  })}
                </div>
                <div className="flex flex-col gap-2.5 pt-4 border-t border-surface-border text-sm">
                  <div className="flex justify-between">
                    <span className="text-text-secondary">Tạm tính</span>
                    <span className="font-semibold">{formatVND(subtotal)}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-text-secondary">Phí ship</span>
                    <span className="font-semibold text-brand-green">{feeLoading ? "Đang tính..." : feeInfo ? formatVND(ghFee) : (subtotal >= freeShipLimit ? "Miễn phí" : formatVND(baseShippingFee))}</span>
                  </div>
                  {feeInfo && (
                    <div className="text-xs text-text-muted mt-2 space-y-1">
                      <p>Phí dịch vụ: {formatVND(feeInfo.serviceFee || 0)}</p>
                      <p>Phí bảo hiểm: {formatVND(feeInfo.insuranceFee || 0)}</p>
                      {feeInfo.pickStationFee != null && <p>Phí nhận hàng: {formatVND(feeInfo.pickStationFee)}</p>}
                      {feeInfo.r2sFee != null && <p>Phí r2s: {formatVND(feeInfo.r2sFee)}</p>}
                    </div>
                  )}
                  {feeError && <p className="text-xs text-danger mt-2">{feeError}</p>}
                </div>
                <div className="flex justify-between pt-4 mb-6 mt-2 border-t border-surface-border">
                  <span className="font-bold text-text-primary">Tổng cộng</span>
                  <span className="text-xl font-extrabold text-brand-blue">{formatVND(subtotal + shipping)}</span>
                </div>
                <button type="submit" disabled={submitting} className="w-full btn-primary py-4 rounded-xl text-base disabled:opacity-60 active:scale-95 transition-transform">
                  {submitting
                    ? <><span className="material-symbols-outlined animate-spin" style={{ fontSize: 18 }}>progress_activity</span> Đang xử lý...</>
                    : <><span className="material-symbols-outlined" style={{ fontSize: 20 }}>check_circle</span> Xác nhận đặt hàng</>
                  }
                </button>
                <p className="mt-3 text-center text-xs text-text-muted">🔒 Giao dịch được bảo mật tuyệt đối</p>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}