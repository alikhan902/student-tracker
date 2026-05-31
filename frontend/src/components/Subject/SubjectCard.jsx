import React from 'react';
import toast from 'react-hot-toast';
import { useEffect, useState } from 'react';

export const SubjectCard = ({ title, url, onClick, isDeleteVisible, onDelete }) => {
  
  const [img, setImage] = useState(null);

  useEffect(() => {
    console.log(url);
  }, [url]);

  return (
    <div onClick={onClick} className="group relative w-full h-[200px] rounded-2xl overflow-hidden bg-white shadow-md hover:shadow-xl transition-all duration-300 cursor-pointer hover:-translate-y-1">
      
      <img
        src={url}
        alt={title}
        className="absolute inset-0 w-full h-full object-cover duration-500"
      />

      <div className="absolute inset-0 bg-gradient-to-t from-black/70 via-black/30" />

      {isDeleteVisible && (
        <button onClick={(e) => {
          e.stopPropagation();
          onDelete();
        }} className="absolute top-2 right-2 bg-red-500 text-white px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition-opacity">
          Удалить предмет
        </button>
      )}

      <div className="relative z-10 h-50 top-[138px] flex items-end p-4 bg-[rgba(0,0,0,0.5)]">
        <h2 className="text-white text-lg font-semibold">
          {title}
        </h2>
      </div>
    </div>
  );
};