# Constants
cd_volume_m3 = 1.0  # 1 cubic meter of CDs
tungsten_density = 19300  # kg/m³
polyethylene_density = 950  # kg/m³ (high-density polyethylene)

# Estimated thicknesses required
tungsten_thickness_m = 0.025  # 2.5 cm for gamma shielding
polyethylene_thickness_m = 0.10  # 10 cm for neutron shielding

# Surface area of a cube enclosing 1 m³ volume
surface_area_m2 = 6 * 1 * 1  # m²

# Volume of shielding materials needed
tungsten_volume_m3 = surface_area_m2 * tungsten_thickness_m
polyethylene_volume_m3 = surface_area_m2 * polyethylene_thickness_m

# Mass of each material
tungsten_mass_kg = tungsten_volume_m3 * tungsten_density
polyethylene_mass_kg = polyethylene_volume_m3 * polyethylene_density

# Prices (approximate market rates)
tungsten_price_per_kg = 100  # USD/kg
polyethylene_price_per_kg = 2  # USD/kg

# Costs
tungsten_cost = tungsten_mass_kg * tungsten_price_per_kg
polyethylene_cost = polyethylene_mass_kg * polyethylene_price_per_kg
total_cost = tungsten_cost + polyethylene_cost

# Output results
print("🔒 Tungsten Shielding:")
print(f" - Thickness: {tungsten_thickness_m*100:.1f} cm")
print(f" - Mass: {tungsten_mass_kg:.1f} kg")
print(f" - Cost: ${tungsten_cost:,.2f} USD")

print("\n🛡️ Polyethylene Shielding:")
print(f" - Thickness: {polyethylene_thickness_m*100:.1f} cm")
print(f" - Mass: {polyethylene_mass_kg:.1f} kg")
print(f" - Cost: ${polyethylene_cost:,.2f} USD")

print(f"\n💰 Total Estimated Cost: ${total_cost:,.2f} USD")
"""
🧱 Giải pháp che chắn thực tế hơn vonfram:
Vonfram rất tốt nhưng quá đắt và khó gia công với số lượng lớn. Dưới đây là phương án thiết thực hơn cho một kho lưu trữ an toàn "dạng tận thế" cho 1 m³ đĩa CD:

🔰 Cấu trúc gợi ý:
Lớp bảo vệ	Vật liệu	Độ dày khuyến nghị	Mục đích
1. Lõi trong	Thùng thép không gỉ hoặc nhôm dày chứa CD	—	Chống ẩm, va chạm cơ học
2. Lớp neutron	Polyethylene + Boron	10 cm	Làm chậm và hấp thụ neutron
3. Lớp gamma	Chì hoặc thép dày	2.5–5 cm chì / 10 cm thép	Chắn tia gamma
4. Lớp cơ học ngoài cùng	Bê tông, đất nén, container	30–50 cm	Chống nổ, cháy, va chạm, thời tiết
(Tùy chọn)	Lớp vonfram mỏng (~1–2 cm)	Nếu ngân sách cho phép	Bảo vệ tối đa trước gamma cực mạnh

💰 Chi phí tham khảo (gần đúng, giá công nghiệp):
Vật liệu	Khối lượng (ước lượng)	Chi phí (USD)
Polyethylene (HDPE + boron)	~600 kg	~$1,200
Chì (3–5 cm dày)	~2,500–4,000 kg	~$7,500–$12,000
Thép (10 cm dày)	~5,000 kg	~$3,000–$6,000
Vonfram (nếu dùng)	~2,300 kg	~$230,000
Tổng phương án không dùng vonfram	—	~$10,000–$20,000
"""