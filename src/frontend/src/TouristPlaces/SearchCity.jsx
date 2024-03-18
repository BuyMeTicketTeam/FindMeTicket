import React, { useState } from 'react';
import AsyncSelect from 'react-select/async';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import useGetCities from '../hook/useGetCities';
import Button from '../utils/Button';

export default function SearchCity() {
  const [city, setCity] = useState('');
  const [errorCity, setErrorCity] = useState(false);
  const getCities = useGetCities();
  const { t } = useTranslation('translation', { keyPrefix: 'search' });
  const navigate = useNavigate();
  const noOptionsMessage = (target) => (target.inputValue.length > 1 ? (t('error')) : null);

  function findPlaces() {
    if (!city) {
      setErrorCity(true);
      return;
    }
    navigate(`/tourist-places/${city.value}`);
  }

  return (
    <div className="search-city">
      <div className="tourist-places-background" />
      <div className="search-city-form">
        <h1 className="search-city-form__title">Пошук туристичних місць:</h1>
        <AsyncSelect
          isClearable
          value={city}
          noOptionsMessage={noOptionsMessage}
          loadingMessage={() => t('loading')}
          cacheOptions
          classNamePrefix="react-select"
          loadOptions={getCities}
          placeholder="Одеса"
          onChange={setCity}
          onInputChange={() => setErrorCity(false)}
        />
        <Button
          name="Пошук"
          className="search-city-form__button"
          onButton={() => findPlaces()}
        />
        {errorCity && <p className="search-field__error">{t('error2')}</p>}
      </div>
    </div>
  );
}
