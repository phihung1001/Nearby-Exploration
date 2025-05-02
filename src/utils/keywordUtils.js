const stopWords = [
    "là", "của", "và", "với", "một", "món", "cho", "ở", "các", "đặc", "biệt", "đến",
    "trong", "tại", "nơi", "có", "được", "hay", "bằng", "như", "rất", "nhiều", "ít",
    "nhưng", "thì", "lại", "đã", "sẽ", "này", "kia", "ấy", "đó", "đây", "những", "khi"
  ];
  
const compoundKeywords = [
    "miền trung", "miền nam", "miền bắc", "miền tây","tây bắc", "tây nguyên",
    "đông bắc", "đông nam", "nam bộ", "bắc bộ", "bắc trung bộ","xứ nghệ", "xứ quảng",
    "hà nội", "sài gòn", "hải phòng", "đà nẵng", "huế", "nha trang","hà tĩnh", "hà giang",
    "quảng bình", "quảng trị", "thừa thiên huế", "khánh hòa", "bình định",
    "hàn quốc", "hồng kông", "thái lan", "trung quốc", "nhật bản",
    "gà quay", "vịt quay", "bò kho", "bò né", "mì quảng", "bún bò", "bún chả", "bún đậu",
    "cơm tấm", "cơm gà", "cơm sườn", "cơm rang", "cơm cháy",
    "phở bò", "phở gà", "hủ tiếu", "bánh canh", "cháo lòng", "xôi gà",
    "lẩu thái", "lẩu cá", "lẩu gà", "lẩu bò", "lẩu hải sản",
    "trà sữa", "trà đào", "trà chanh", "nước mía", "nước ép", "cà phê", "sinh tố",
    "soda", "nước ngọt", "sữa chua", "sữa tươi", "sữa đậu nành",
    "bánh kem", "bánh sinh nhật", "bánh trung thu", "bánh sinh nhật", "bánh quy",
    "kem tươi", "bánh mì", "bánh xèo", "bánh cuốn", "bánh tráng", "bánh bèo", "bánh hỏi", "bánh khọt",
    "gỏi cuốn", "nem nướng", "ốc len", "ốc hương", "ghẹ hấp", "cua rang me"
];

// Hàm chuẩn hóa (loại bỏ dấu, chuyển về chữ thường)
const normalizeText = (text) => {
  return text.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");
};

export const getSearchKeywords = (dishName) => {
  // Chuẩn hóa dishName (loại bỏ dấu, chuyển về chữ thường)
  let name = normalizeText(dishName);

  // Mảng lưu các cụm từ đã tìm thấy
  const matchedCompounds = [];

  // Tìm và giữ nguyên các compoundKeywords
  compoundKeywords.forEach(compound => {
    // Chuẩn hóa compoundKeyword trước khi so sánh
    const normalizedCompound = normalizeText(compound);
    
    // Kiểm tra xem cụm từ có trong name không
    if (name.includes(normalizedCompound)) {
      matchedCompounds.push(compound);  // Lưu lại cụm từ tìm thấy
      // Chỉ thay thế nếu tìm thấy, giữ nguyên các compound đã nhận diện
      name = name.replace(normalizedCompound, ""); // Xóa cụm từ khỏi chuỗi gốc để tránh bị tách
    }
  });

  // Tách các từ còn lại thành từ đơn và loại bỏ stopWords
  const words = name
    .split(" ")
    .map(word => word.trim())
    .filter(word => word && !stopWords.includes(word));

  // Trả về mảng bao gồm compound và các từ đơn
  return [...matchedCompounds, ...words];  
};