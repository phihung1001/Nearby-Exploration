export const getCoordinatesFromLocation = async (locationName) => {
    const response = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${locationName}`);
    if (!response.ok) {
      throw new Error('Lỗi khi gọi OpenStreetMap API!');
    }
  
    const data = await response.json();
    if (!data || data.length === 0) {
      throw new Error('Không tìm thấy địa điểm!');
    }
  
    return { lat: data[0].lat, lng: data[0].lon };
  };
  