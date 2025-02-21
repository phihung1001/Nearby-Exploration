import React, { useState } from "react";
import image1 from "../../assets/Img/image1.png";
import image2 from "../../assets/Img/image2.png";
import "./LanguageSwitcher.css"; // Import CSS

export default function LanguageSwitcher() {
  const [currentImage, setCurrentImage] = useState(image1);

  const handleToggleImage = () => {
    setCurrentImage(currentImage === image1 ? image2 : image1);
    console.log(currentImage === image1 ? "English" : "Vietnam");
  };

  return (
    <button className="language-button" onClick={handleToggleImage}>
      <img src={currentImage} alt="Language Icon" style={{ width: "30px", height: "30px" }} />
    </button>
  );
}
