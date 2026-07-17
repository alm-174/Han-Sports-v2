package com.javaweb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.domain.AppSetting;
import com.javaweb.repository.AppSettingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppSettingSeeder implements CommandLineRunner {

    private final AppSettingRepository appSettingRepository;

    public AppSettingSeeder(AppSettingRepository appSettingRepository) {
        this.appSettingRepository = appSettingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // HERO_SLIDES
        List<Map<String, String>> slides = new ArrayList<>();
        Map<String, String> slide1 = new HashMap<>();
        slide1.put("title", "Vợt Yonex Astrox 99 Pro");
        slide1.put("subtitle", "Sức mạnh tối đa — Kiểm soát tuyệt vời");
        slide1.put("cta", "Mua ngay");
        slide1.put("ctaLink", "/shop?q=Yonex");
        slide1.put("bg", "from-[#0f2027] via-[#1d4ed8] to-[#0d9488]");
        slides.add(slide1);

        Map<String, String> slide2 = new HashMap<>();
        slide2.put("title", "Giày Victor S82 III");
        slide2.put("subtitle", "Nhẹ — Bền — Thoải mái");
        slide2.put("cta", "Xem bộ sưu tập");
        slide2.put("ctaLink", "/shop?category=giay-cau-long");
        slide2.put("bg", "from-[#0f2027] via-[#16a34a] to-[#0d9488]");
        slides.add(slide2);

        upsertSetting("HERO_SLIDES", mapper.writeValueAsString(slides));

        // CATEGORIES
        List<Map<String, String>> cats = new ArrayList<>();
        cats.add(createCat("Vợt Cầu Lông", "sports_tennis", "/shop?category=vot-cau-long", "bg-brand-blue-light text-brand-blue"));
        cats.add(createCat("Giày Cầu Lông", "footprint", "/shop?category=giay-cau-long", "bg-brand-green-light text-brand-green"));
        cats.add(createCat("Quần Áo", "dry_cleaning", "/shop?category=quan-ao", "bg-brand-teal-light text-brand-teal"));
        cats.add(createCat("Balo - Túi", "backpack", "/shop?category=balo-tui", "bg-blue-50 text-blue-600"));
        cats.add(createCat("Phụ Kiện", "category", "/shop?category=phu-kien", "bg-green-50 text-green-600"));
        cats.add(createCat("Khuyến Mãi", "local_fire_department", "/shop?sale=true", "bg-red-50 text-danger"));

        upsertSetting("CATEGORIES", mapper.writeValueAsString(cats));

        // BRANDS
        upsertSetting("BRANDS", "[\"Yonex\", \"Victor\", \"Lining\", \"Kawasaki\", \"Mizuno\", \"Apacs\", \"Flypower\", \"Kumpoo\", \"Khác\"]");

        // TARGETS
        upsertSetting("TARGETS", "[\"Nam\", \"Nữ\", \"Unisex\", \"Trẻ em\"]");

        // HOTLINE
        upsertSetting("HOTLINE", "090 123 4567");

        // SHIPPING_FEE
        upsertSetting("SHIPPING_FEE", "30000");

        // FREE_SHIP_LIMIT
        upsertSetting("FREE_SHIP_LIMIT", "500000");

        System.out.println(">>> SEEDED APP SETTINGS");
    }

    private void upsertSetting(String settingKey, String settingValue) {
        AppSetting setting = appSettingRepository.findBySettingKey(settingKey)
                .orElseGet(AppSetting::new);
        setting.setSettingKey(settingKey);
        setting.setSettingValue(settingValue);
        appSettingRepository.save(setting);
    }

    private Map<String, String> createCat(String name, String icon, String path, String color) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("icon", icon);
        map.put("path", path);
        map.put("color", color);
        return map;
    }
}
