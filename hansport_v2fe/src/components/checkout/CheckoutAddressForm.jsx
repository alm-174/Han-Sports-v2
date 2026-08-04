import { useState, useEffect } from "react";
import { ghnApi } from "../../api/ghnApi";

const EMPTY_ADDRESS = {
  provinceId: null,
  provinceName: "",
  districtId: null,
  districtName: "",
  wardCode: "",
  wardName: "",
  addressDetail: "",
};

export default function CheckoutAddressForm({
  receiverName,
  receiverPhone,
  addressDetail: initialAddressDetail = "",
  note = "",
  onReceiverChange,
  onAddressChange,
  onNoteChange,
  onFeeChange,
  insuranceValue = 0,
}) {
  const [provinces, setProvinces] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [wards, setWards] = useState([]);
  const [address, setAddress] = useState({ ...EMPTY_ADDRESS, addressDetail: initialAddressDetail });
  const [feeInfo, setFeeInfo] = useState(null);
  const [feeLoading, setFeeLoading] = useState(false);
  const [feeError, setFeeError] = useState(null);

  const GHN_FROM_DISTRICT_ID = Number(import.meta.env.VITE_GHN_FROM_DISTRICT_ID || 0);
  const GHN_FROM_WARD_CODE = import.meta.env.VITE_GHN_FROM_WARD_CODE || "";

  useEffect(() => {
    ghnApi.getProvinces()
      .then((res) => setProvinces(res.data?.data || res.data || []))
      .catch(() => setProvinces([]));
  }, []);

  useEffect(() => {
    if (!address.provinceId) {
      setDistricts([]);
      setWards([]);
      return;
    }

    setDistricts([]);
    setWards([]);
    setAddress((prev) => ({
      ...prev,
      districtId: null,
      districtName: "",
      wardCode: "",
      wardName: "",
    }));

    ghnApi.getDistricts(address.provinceId)
      .then((res) => setDistricts(res.data?.data || res.data || []))
      .catch(() => setDistricts([]));
  }, [address.provinceId]);

  useEffect(() => {
    if (!address.provinceId || !address.districtId) {
      setWards([]);
      return;
    }

    setWards([]);
    setAddress((prev) => ({ ...prev, wardCode: "", wardName: "" }));

    ghnApi.getWards(address.districtId)
      .then((res) => setWards(res.data?.data || res.data || []))
      .catch(() => setWards([]));
  }, [address.districtId, address.provinceId]);

  useEffect(() => {
    onAddressChange?.(address);
  }, [address]);

  useEffect(() => {
    const ready =
      address.provinceId &&
      address.districtId &&
      address.wardCode &&
      address.addressDetail?.trim() &&
      GHN_FROM_DISTRICT_ID &&
      GHN_FROM_WARD_CODE;

    if (!ready) {
      setFeeInfo(null);
      setFeeError(null);
      onFeeChange?.(null, false, null);
      return;
    }

    setFeeLoading(true);
    setFeeError(null);
    ghnApi.getFee({
      toDistrictId: address.districtId,
      toWardCode: String(address.wardCode),
      insuranceValue: Math.round(insuranceValue),
      coupon: null,
    })
      .then((res) => {
        const data = res.data?.data || res.data || null;
        setFeeInfo(data);
        onFeeChange?.(data, false, null);
      })
      .catch(() => {
        setFeeInfo(null);
        setFeeError("Không tính được phí ship ước tính");
        onFeeChange?.(null, false, "Không tính được phí ship ước tính");
      })
      .finally(() => setFeeLoading(false));
  }, [
    address.provinceId,
    address.districtId,
    address.wardCode,
    address.addressDetail,
    insuranceValue,
  ]);

  const handleProvinceChange = (e) => {
    const provinceId = Number(e.target.value) || null;
    const province = provinces.find((p) => p.provinceId === provinceId);
    setAddress((prev) => ({
      ...prev,
      provinceId,
      provinceName: province?.provinceName || "",
      districtId: null,
      districtName: "",
      wardCode: "",
      wardName: "",
    }));
  };

  const handleDistrictChange = (e) => {
    const districtId = Number(e.target.value) || null;
    const district = districts.find((d) => d.districtId === districtId);
    setAddress((prev) => ({
      ...prev,
      districtId,
      districtName: district?.districtName || "",
      wardCode: "",
      wardName: "",
    }));
  };

  const handleWardChange = (e) => {
    const wardCode = e.target.value;
    const ward = wards.find((w) => String(w.wardCode) === String(wardCode));
    setAddress((prev) => ({
      ...prev,
      wardCode: wardCode ? String(wardCode) : "",
      wardName: ward?.wardName || "",
    }));
  };

  const handleAddressDetailChange = (e) => {
    setAddress((prev) => ({ ...prev, addressDetail: e.target.value }));
  };

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div>
        <label className="block text-sm font-semibold text-text-secondary mb-2">Họ và tên *</label>
        <input
          name="receiverName"
          required
          value={receiverName}
          onChange={onReceiverChange}
          placeholder="Nguyễn Văn A"
          className="input-field"
        />
      </div>
      <div>
        <label className="block text-sm font-semibold text-text-secondary mb-2">Số điện thoại *</label>
        <input
          name="receiverPhone"
          required
          value={receiverPhone}
          onChange={onReceiverChange}
          placeholder="090 123 4567"
          type="tel"
          className="input-field"
        />
      </div>

      <div>
        <label className="block text-sm font-semibold text-text-secondary mb-2">Tỉnh/Thành phố *</label>
        <select
          name="provinceId"
          value={address.provinceId || ""}
          onChange={handleProvinceChange}
          required
          className="input-field"
        >
          <option value="">Chọn Tỉnh/Thành</option>
          {provinces.map((p) => (
            <option key={p.provinceId} value={p.provinceId}>
              {p.provinceName} ({p.provinceId})
            </option>
          ))}
        </select>
      </div>

      <div>
        <label className="block text-sm font-semibold text-text-secondary mb-2">
          Quận/Huyện *
          {address.provinceId && (
            <span className="text-xs text-text-muted block mt-1">
              Tỉnh đang chọn: {address.provinceName} ({address.provinceId})
            </span>
          )}
        </label>
        <select
          name="districtId"
          value={address.districtId || ""}
          onChange={handleDistrictChange}
          required
          disabled={!address.provinceId}
          className="input-field"
        >
          <option value="">Chọn Quận/Huyện</option>
          {districts.map((d) => (
            <option key={d.districtId} value={d.districtId}>
              {d.districtName} ({d.districtId})
            </option>
          ))}
        </select>
      </div>

      <div>
        <label className="block text-sm font-semibold text-text-secondary mb-2">Xã/Phường *</label>
        <select
          name="wardCode"
          value={address.wardCode}
          onChange={handleWardChange}
          required
          disabled={!address.districtId}
          className="input-field"
        >
          <option value="">Chọn Xã/Phường</option>
          {wards.map((w) => (
            <option key={w.wardCode} value={w.wardCode}>{w.wardName}</option>
          ))}
        </select>
      </div>

      <div>
        <label className="block text-sm font-semibold text-text-secondary mb-2">Địa chỉ chi tiết *</label>
        <input
          name="addressDetail"
          required
          value={address.addressDetail}
          onChange={handleAddressDetailChange}
          placeholder="Số nhà, tên đường, khu vực..."
          className="input-field"
        />
      </div>

      <div className="md:col-span-2">
        <label className="block text-sm font-semibold text-text-secondary mb-2">Ghi chú (tùy chọn)</label>
        <textarea
          name="note"
          value={note}
          onChange={onNoteChange}
          rows={3}
          placeholder="Ghi chú cho đơn hàng (ví dụ: giao trong giờ hành chính...)"
          className="input-field resize-none"
        />
      </div>

      {feeLoading && (
        <div className="md:col-span-2 text-xs text-text-muted">Đang tính phí ship ước tính...</div>
      )}
      {feeError && (
        <div className="md:col-span-2 text-xs text-text-muted">{feeError} — Bạn vẫn có thể đặt hàng bình thường.</div>
      )}
      {feeInfo && !feeLoading && (
        <div className="md:col-span-2 text-xs text-text-muted">
          Phí ship ước tính (GHN):{" "}
          {(feeInfo.serviceFee || 0) + (feeInfo.insuranceFee || 0) + (feeInfo.pickStationFee || 0) + (feeInfo.r2sFee || 0)} đ
        </div>
      )}
    </div>
  );
}
