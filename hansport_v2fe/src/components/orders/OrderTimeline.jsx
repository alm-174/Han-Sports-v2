import { ORDER_TIMELINE_STEPS, getOrderStatusInfo, normalizeOrderStatus } from "../../utils/orderStatus";

export default function OrderTimeline({ status, ghnStatus }) {
  const normalized = normalizeOrderStatus(status);
  const isCancelled = normalized === "DA_HUY";
  const currentStep = getOrderStatusInfo(status).step;

  if (isCancelled) {
    return (
      <div className="p-4 bg-red-50 rounded-2xl border border-red-100 text-sm">
        <p className="font-bold text-red-600 flex items-center gap-2">
          <span className="material-symbols-outlined" style={{ fontSize: 18 }}>cancel</span>
          Đơn hàng đã bị hủy
        </p>
        {ghnStatus && <p className="text-xs text-red-500 mt-1">GHN: {ghnStatus}</p>}
      </div>
    );
  }

  return (
    <div className="space-y-1">
      <p className="text-xs font-bold text-text-secondary uppercase tracking-widest mb-3">Tiến trình đơn hàng</p>
      <div className="flex flex-col gap-0">
        {ORDER_TIMELINE_STEPS.map((step, index) => {
          const done = currentStep > index;
          const active = currentStep === index;
          return (
            <div key={step.key} className="flex gap-3">
              <div className="flex flex-col items-center">
                <div className={`w-8 h-8 rounded-full flex items-center justify-center flex-shrink-0 ${
                  done ? "bg-brand-green text-white" : active ? "bg-brand-blue text-white" : "bg-surface-muted text-text-muted"
                }`}>
                  <span className="material-symbols-outlined" style={{ fontSize: 16, fontVariationSettings: done || active ? "'FILL' 1" : undefined }}>
                    {done ? "check" : step.icon}
                  </span>
                </div>
                {index < ORDER_TIMELINE_STEPS.length - 1 && (
                  <div className={`w-0.5 h-6 ${done ? "bg-brand-green" : "bg-surface-border"}`} />
                )}
              </div>
              <div className={`pb-4 pt-1 ${active ? "font-bold text-brand-blue" : done ? "text-text-primary" : "text-text-muted"}`}>
                <p className="text-sm">{step.label}</p>
                {active && ghnStatus && (
                  <p className="text-[10px] text-text-muted mt-0.5 font-normal">GHN: {ghnStatus}</p>
                )}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
