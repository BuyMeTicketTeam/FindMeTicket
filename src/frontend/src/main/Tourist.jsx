import React, { useState } from 'react';

function Banner() {
  const [isBannerVisible, setIsBannerVisible] = useState(true);

  const closeBanner = () => {
    setIsBannerVisible(false);
  };

  const handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      closeBanner();
    }
  };

  return (
    <div>
      {isBannerVisible && (
        <div className="banner-container">
          <div className="centered-content">
            <p className="banner-text">
              У нас є кілька цікавих місць, які можете відвідати у цьому місті
            </p>
            <div className="details-button">
              <button type="button">Детальніше тут</button>
            </div>
          </div>
          <div className="close-button-container">
            <div
              className="close-button"
              role="button"
              tabIndex="0"
              onClick={closeBanner}
              onKeyPress={handleKeyPress}
            >
              ✖
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Banner;
