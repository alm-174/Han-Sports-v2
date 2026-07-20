// locations helper. Tries to load a local `vn_locations.json` (if present),
// otherwise fetches from the public API at build/runtime and caches results.

let CACHE = { provinces: [] };

async function fetchFromApi() {
  const url = "https://provinces.open-api.vn/api/?depth=3";
  const res = await fetch(url);
  if (!res.ok) throw new Error('Failed to fetch locations');
  const data = await res.json();
  return data;
}

function normalize(data) {
  return data.map((p) => ({
    code: String(p.code),
    name: p.name,
    districts: (p.districts || []).map((d) => ({
      code: String(d.code),
      name: d.name,
      wards: (d.wards || []).map((w) => ({ code: String(w.code), name: w.name })),
    })),
  }));
}

export async function loadLocations() {
  if (CACHE.provinces.length) return CACHE.provinces;
  // try to load bundled JSON first
  // try to load a bundled JSON placed at the public root: /vn_locations.json
  try {
    const localRes = await fetch('/vn_locations.json');
    if (localRes.ok) {
      const localData = await localRes.json();
      CACHE.provinces = normalize(localData);
      return CACHE.provinces;
    }
  } catch (e) {
    // ignore and fall back to API
  }

  try {
    const data = await fetchFromApi();
    CACHE.provinces = normalize(data);
    return CACHE.provinces;
  } catch (err) {
    // final fallback: small embedded defaults
    CACHE.provinces = [
      { code: '1', name: 'Hà Nội', districts: [] },
      { code: '79', name: 'TP. Hồ Chí Minh', districts: [] },
    ];
    return CACHE.provinces;
  }
}

export function getProvinces() {
  return CACHE.provinces.map((p) => ({ code: p.code, name: p.name }));
}

export function getDistricts(provinceCode) {
  const p = CACHE.provinces.find((x) => String(x.code) === String(provinceCode));
  return p ? p.districts.map((d) => ({ code: d.code, name: d.name })) : [];
}

export function getWards(provinceCode, districtCode) {
  const p = CACHE.provinces.find((x) => String(x.code) === String(provinceCode));
  if (!p) return [];
  const d = p.districts.find((x) => String(x.code) === String(districtCode));
  return d ? d.wards.map((w) => ({ code: w.code, name: w.name })) : [];
}

export function getProvinceName(code) {
  const p = CACHE.provinces.find((x) => String(x.code) === String(code));
  return p ? p.name : '';
}

export function getDistrictName(provinceCode, districtCode) {
  const p = CACHE.provinces.find((x) => String(x.code) === String(provinceCode));
  if (!p) return '';
  const d = p.districts.find((x) => String(x.code) === String(districtCode));
  return d ? d.name : '';
}

export function getWardName(provinceCode, districtCode, wardCode) {
  const p = CACHE.provinces.find((x) => String(x.code) === String(provinceCode));
  if (!p) return '';
  const d = p.districts.find((x) => String(x.code) === String(districtCode));
  if (!d) return '';
  const w = d.wards.find((x) => String(x.code) === String(wardCode));
  return w ? w.name : '';
}

export default {
  loadLocations,
  getProvinces,
  getDistricts,
  getWards,
  getProvinceName,
  getDistrictName,
};
