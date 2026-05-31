import { useState, useEffect } from 'react';
import toast from 'react-hot-toast';
import axios from 'axios';
import { materialCompleteService } from '../../api/index';


export const SubjectSection = ({ title, description, originalFileName, fileDownloadUrl, materialId, isSettingsVisible, OnDelete, OnChanges }) => {
    const [isOpen, setIsOpen] = useState(false);
    const [completionStatus, setCompletionStatus] = useState(null);
    const [isLoadingStatus, setIsLoadingStatus] = useState(false);
    const [isTogglingStatus, setIsTogglingStatus] = useState(false);

    // Load completion status on mount
    useEffect(() => {
        if (materialId) {
            loadCompletionStatus();
        }
    }, [materialId]);

    const loadCompletionStatus = async () => {
        setIsLoadingStatus(true);
        try {
            const response = await materialCompleteService.getStatus(materialId);
            setCompletionStatus(response.data.status);
        } catch (error) {
            // 404 means no record yet (not an error)
            if (error.response?.status !== 404) {
                console.error('Ошибка загрузки статуса:', error);
            }
            setCompletionStatus(null);
        } finally {
            setIsLoadingStatus(false);
        }
    };

    const handleToggleCompletion = async (e) => {
        e.stopPropagation();
        setIsTogglingStatus(true);
        try {
            const newStatus = completionStatus === 'COMPLETED' ? 'NOT_COMPLETED' : 'COMPLETED';
            const response = await materialCompleteService.updateStatus(materialId, newStatus);
            setCompletionStatus(response.data.status);
            toast.success(newStatus === 'COMPLETED' ? 'Материал отмечен как выполненный' : 'Материал отмечен как невыполненный');
        } catch (error) {
            console.error('Ошибка обновления статуса:', error);
            toast.error('Ошибка обновления статуса');
        } finally {
            setIsTogglingStatus(false);
        }
    };

    const isCompleted = completionStatus === 'COMPLETED';

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
            </div>

            <div className={`overflow-hidden transition-all duration-300 px-5 ${isOpen ? 'max-h-80 pb-5 opacity-100' : 'max-h-0 opacity-0'}`}>
                <p className="text-sm text-gray-600 mb-4 line-clamp-2">
                    {description || "Описание отсутствует"}
                </p>

                <div className="flex items-center justify-between gap-2 flex-wrap">
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

                    <button
                    onClick={handleToggleCompletion}
                    disabled={isTogglingStatus}
                    className={`text-xs px-3 py-1.5 rounded-md transition-all ${
                        isCompleted 
                        ? 'bg-green-500 text-white hover:bg-green-600' 
                        : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                    } disabled:opacity-50`}
                    title={isCompleted ? 'Отметить как невыполненное' : 'Отметить как выполненное'}
                    >
                        {isTogglingStatus ? '...' : (isCompleted ? '✓ Выполнено' : '○ Не выполнено')}
                    </button>

                {isSettingsVisible && (   
                    <>
                        <button
                        onClick={(e) => {
                            e.stopPropagation();
                            OnChanges();
                        }}
                        className="text-xs bg-primary text-white px-3 py-1.5 rounded-md hover:bg-primary-hover transition-colors"
                        >
                            Изменить
                        </button>

                        <button
                        onClick={(e) => {
                            e.stopPropagation();
                            OnDelete();
                        }}
                        className="text-xs bg-red-500 text-white px-3 py-1.5 rounded-md hover:bg-red-600 transition-colors"
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