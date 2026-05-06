import { useState } from 'react';
import toast from 'react-hot-toast';
import axios from 'axios';


export const SubjectSection = ({ title, description, originalFileName, uploadAt, fileDownloadUrl, isSettingsVisible, OnDelete, OnChange }) => {
    const [isOpen, setIsOpen] = useState(false);


    const handleDownloadFile = async (fileUrl, fileName) => {
        try {
            const response = await axios.get(fileUrl, {
                responseType: 'blob',
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('accessToken')}`
                }
            });

            const blobUrl = window.URL.createObjectURL(response.data);

            const link = document.createElement('a');
            link.href = blobUrl;
            link.setAttribute('download', fileName);

            document.body.appendChild(link);
            link.click();
            link.remove();

            window.URL.revokeObjectURL(blobUrl);

            toast.success("Файл скачан");

        } catch (error) {
            console.error('Ошибка скачивания:', error);
            toast.error("Ошибка скачивания файла");
        }
    };

    return (
        <div className="bg-white border border-lightblue-border rounded-2xl shadow-sm transition-all duration-300">
        
            <div onClick={() => setIsOpen(!isOpen)} className="group p-5 cursor-pointer hover:bg-lightblue-50 rounded-2xl">
                <div className="flex justify-between items-center">
                <h3 className="text-lg font-semibold text-gray-900 group-hover:text-primary transition-colors">
                    {title}
                </h3>

                <span className={`transition-transform duration-300 ${isOpen ? 'rotate-180' : ''}`}>
                    ▼
                </span>
                </div>

                <span className="text-xs text-gray-400">
                    {uploadAt}
                </span>
            </div>

            <div className={`overflow-hidden transition-all duration-300 px-5 ${isOpen ? 'max-h-40 pb-5 opacity-100' : 'max-h-0 opacity-0'}`}>
                <p className="text-sm text-gray-600 mb-4 line-clamp-2">
                    {description || "Описание отсутствует"}
                </p>

                <div className="flex items-center justify-between">
                    <div className="text-xs text-gray-500 truncate">
                    {originalFileName || "Без файла"}
                    </div>

                    <button
                    onClick={(e) => {
                        e.stopPropagation();
                        handleDownloadFile(fileDownloadUrl, originalFileName);
                    }}
                    className="text-xs bg-primary text-white px-3 py-1.5 rounded-md hover:bg-primary-hover transition-colors"
                    >
                        Загрузить
                    </button>

                {isSettingsVisible && (   
                    <>
                        <button
                        onClick={(e) => {
                            e.stopPropagation();
                            onChange();
                        }}
                        className="text-xs bg-primary text-white px-3 py-1.5 rounded-md hover:bg-primary-hover transition-colors"
                        >
                            Изменить
                        </button>

                        <button
                        onClick={(e) => {
                            e.stopPropagation();
                            onDelete();
                        }}
                        className="text-xs bg-primary text-white px-3 py-1.5 rounded-md bg-red-500 hover:bg-red-600 transition-colors"
                        >
                            Удалить
                        </button>
                    </>
                )}
                </div>
                <div className="absolute inset-0 rounded-2xl ring-1 ring-transparent group-hover:ring-primary transition pointer-events-none" />
            </div>
        </div>
    );
};