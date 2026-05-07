import { SubjectSection } from '../components/Subject/SubjectSection';
import { useParams } from "react-router-dom";
import { subjectService, educationalMaterialService } from '../api';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import { Plus } from 'lucide-react';
import { Modal } from '../components/ui/Modal';
import { useAuth } from '../context/AuthContext';

export default function SubjectPage() {
 
    const { user, setUser, logout } = useAuth();

    const navigate = useNavigate();

    const id = useParams().id;

    const [isCreateMaterial, setIsCreateMaterial] = useState(false);
    const [isDeleteMaterial, setIsDeleteMaterial] = useState(false);
    const [isUpdateMaterial, setIsUpdateMaterial] = useState(false);

    const [loading, setLoading] = useState(false);
    const [selectedMaterial, setSelectedMaterial] = useState(null);

    const [materialTitle, setMaterialTitle] = useState('');
    const [materialDescription, setMaterialDescription] = useState('');
    const [materialFile, setMaterialFile] = useState(null);

    const [subject, setSubject] = useState(null);
    const [materials, setMaterials] = useState([]);

    const fetchSubject = async () => {
        try{
            const subject = await subjectService.getSubject(id);
            setSubject(subject.data);
            if(subject == null || subject == undefined) {
                navigate('/subjects');
            }
        } catch (error) {
            console.error("Error fetching subject:", error?.response?.data?.error ?? error.message);
        }
    };

    const fetchMaterial = async () => {
        try{
            const materials = await educationalMaterialService.getMaterials(id);
            setMaterials(materials.data);
            console.log(materials.data);
        } catch (error) {
            console.error("Error fetching material:", error?.response?.data?.error ?? error.message);
        }
    };

    const handleCreateMaterial = async (e) => {
        e.preventDefault();
        if(!materialTitle || !materialDescription || !materialFile) {
            toast.error('Пожалуйста, заполните все поля и выберите файл.');
            return;
        }
        if(materialFile.size > 10 * 1024 * 1024) {
            toast.error('Размер файла не должен превышать 10 МБ.');
            return;
        }
        setLoading(true);
        try {
            const formData = new FormData();
            const dto = {
                trainingSubjectId: id,
                title: materialTitle,
                description: materialDescription
            };
            formData.append("data", JSON.stringify(dto));
            formData.append("file", materialFile);
            console.log(formData.get("data"));
            await educationalMaterialService.createMaterial(formData);
        } catch (error) {
            console.error("Error creating material:", error?.response?.data?.error ?? error.message);
            toast.error('Ошибка при создании материала. Пожалуйста, попробуйте снова.');
        } finally {
            setLoading(false);
            setIsCreateMaterial(false);
        }
    }

    const handleUpdateMaterial = async (e) => {
        e.preventDefault();
        if(!materialTitle || !materialDescription) {
            toast.error('Пожалуйста, заполните все поля и выберите файл.');
            return;
        }
        if(materialFile && materialFile.size > 1 * 1024 * 1024) {
            toast.error('Размер файла не должен превышать 1 МБ.');
            return;
        }
        setLoading(true);
        try {
            const formData = new FormData();
            const dto = {
                trainingSubjectId: id,
                title: materialTitle,
                description: materialDescription
            };
            formData.append("data", JSON.stringify(dto));
            formData.append("file", materialFile);
            console.log(formData.get("data"));
            await educationalMaterialService.updateMaterial(selectedMaterial.id, formData);
        } catch (error) {
            console.error("Error updating material:", error?.response?.data?.error ?? error.message);
            toast.error('Ошибка при обновлении материала. Пожалуйста, попробуйте снова.');
        } finally {
            setLoading(false);
            setIsUpdateMaterial(false);
        }
    }

    const handleDeleteMaterial = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            await educationalMaterialService.deleteMaterial(selectedMaterial.id);
            setMaterials(materials.filter((m) => m.id !== selectedMaterial.id));
            toast.success('Материал успешно удален.');
        } catch (error) {
            console.error("Error deleting material:", error?.response?.data?.error ?? error.message);
            toast.error('Ошибка при удалении материала. Пожалуйста, попробуйте снова.');
        }
        finally {
            setLoading(false);
            setIsDeleteMaterial(false);
        }
    };

    useEffect(() => {
        fetchSubject();
        fetchMaterial();
    }, []);

    return (
        <div className="p-6">
            <h2 className="text-3xl font-heading font-bold text-primary mb-20 text-center">Разделы</h2>
            <div className="flex flex-col gap-6">
                {materials?.map((material) => (
                <SubjectSection key={material.title} title={material.title} description={material.description} originalFileName={material.originalFileName} uploadAt={material.uploadAt} fileDownloadUrl={material.filePath} 
                OnDelete={() => {
                    setSelectedMaterial(material);
                    setIsDeleteMaterial(true);
                }}
                
                OnChanges={() => {
                    setSelectedMaterial(material);
                    setMaterialTitle(material.title);
                    setMaterialDescription(material.description);
                    setIsUpdateMaterial(true);
                }}

                isSettingsVisible={user.studentType == "HEADMAN"}
                />
                ))}
    
            {  (materials != null || materials != undefined) && 
                <div className="flex w-full justify-center">
                    <div onClick={() => setIsCreateMaterial(true)} className="w-24 h-24 border-[5px] border-[#1798ff] rounded-full flex items-center justify-center shadow-lg hover:shadow-xl hover:scale-110 transition-all duration-300 cursor-pointer">
                        <Plus className="text-white text-[#1798ff]" size={32} />
                    </div>
                </div>
            }
            </div>

            <Modal isOpen={isCreateMaterial} onClose={() => setIsCreateMaterial(false)} title="Создать материал">
                <form onSubmit={(e) => handleCreateMaterial(e)} className="space-y-4">
                    <div>
                        <label className="block text-gray-700 mb-5 text-center sm:text-[14px] md:text-[16px] lg:text-[18px]">Название материала</label>
                        <input type="text" placeholder="Введите название материала" className="border border-gray-300 w-full rounded-md py-2 px-4 focus:outline-none focus:ring-2 focus:ring-blue-500" value={materialTitle} onChange={(e) => setMaterialTitle(e.target.value)}/>
                        <label className="block text-gray-700 mb-5 text-center sm:text-[14px] md:text-[16px] lg:text-[18px]">Описание материала</label>
                        <textarea placeholder="Введите описание материала" className="border border-gray-300 w-full min-h-[50px] max-h-[200px] rounded-md py-2 px-4 focus:outline-none focus:ring-2 focus:ring-blue-500" value={materialDescription} onChange={(e) => setMaterialDescription(e.target.value)}/>
                        <label className="block text-gray-700 mb-5 text-center sm:text-[14px] md:text-[16px] lg:text-[18px]">Материала</label>
                        <div className="flex flex-col items-center gap-3">
                            <label className="cursor-pointer bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-6 rounded-lg shadow-md transition duration-200">
                                Выбрать файл
                                <input
                                type="file"
                                className="hidden"
                                onChange={(e) => setMaterialFile(e.target.files[0])}
                                />
                            </label>

                            {materialFile && (
                            <span className="text-sm text-gray-600">
                                {materialFile.name}
                            </span>
                            )}
                        </div>
                    </div>
                    <div className="flex justify-end gap-2 pt-4 mt-4">
                        <button type="button" onClick={() => setIsCreateMaterial(false)} className="px-4 py-2 text-gray-500 hover:text-gray-900">Отмена</button>
                        <button type="submit" disabled={loading} className="px-6 py-2 bg-blue-50 hover:bg-blue-100 text-blue-600 text-sm font-medium py-2.5 px-4 rounded-xl transition-all duration-200 border border-blue-200 hover:shadow-sm">Создать материал</button>
                    </div>
                </form>
            </Modal>

            <Modal isOpen={isUpdateMaterial} onClose={() => setIsUpdateMaterial(false)} title="Обновить материал">
                <form onSubmit={(e) => handleUpdateMaterial(e)} className="space-y-4">
                    <div>
                        <label className="block text-gray-700 mb-5 text-center sm:text-[14px] md:text-[16px] lg:text-[18px]">Название материала</label>
                        <input type="text" placeholder="Введите название материала" className="border border-gray-300 w-full rounded-md py-2 px-4 focus:outline-none focus:ring-2 focus:ring-blue-500" value={materialTitle} onChange={(e) => setMaterialTitle(e.target.value)}/>
                        <label className="block text-gray-700 mb-5 text-center sm:text-[14px] md:text-[16px] lg:text-[18px]">Описание материала</label>
                        <textarea placeholder="Введите описание материала" className="border border-gray-300 w-full min-h-[50px] max-h-[200px] rounded-md py-2 px-4 focus:outline-none focus:ring-2 focus:ring-blue-500" value={materialDescription} onChange={(e) => setMaterialDescription(e.target.value)}/>
                        <label className="block text-gray-700 mb-5 text-center sm:text-[14px] md:text-[16px] lg:text-[18px]">Материала</label>
                        <div className="flex flex-col items-center gap-3">
                            <label className="cursor-pointer bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-6 rounded-lg shadow-md transition duration-200">
                                Выбрать файл
                                <input
                                type="file"
                                className="hidden"
                                onChange={(e) => setMaterialFile(e.target.files[0])}
                                />
                            </label>

                            {materialFile && (
                            <span className="text-sm text-gray-600">
                                {materialFile.name}
                            </span>
                            )}
                        </div>
                    </div>
                    <div className="flex justify-end gap-2 pt-4 mt-4">
                        <button type="button" onClick={() => setIsUpdateMaterial(false)} className="px-4 py-2 text-gray-500 hover:text-gray-900">Отмена</button>
                        <button type="submit" disabled={loading} className="px-6 py-2 bg-blue-50 hover:bg-blue-100 text-blue-600 text-sm font-medium py-2.5 px-4 rounded-xl transition-all duration-200 border border-blue-200 hover:shadow-sm">Обновить материал</button>
                    </div>
                </form>
            </Modal>
            
            <Modal isOpen={isDeleteMaterial} onClose={() => setIsDeleteMaterial(false)} title="Удаление материала">
                <form onSubmit={(e) => handleDeleteMaterial(e)} className="space-y-4">
                    <div>
                        <h2 className="block text-gray-700 mb-5 text-center sm:text-[14px] md:text-[16px] lg:text-[18px]">Удаление материала</h2>
                        <p className="block text-gray-700 mb-5 text-center sm:text-[14px] md:text-[16px] lg:text-[18px]">Вы уверены, что хотите удалить этот материал?</p>
                    </div>
                    <div className="flex justify-end gap-2 pt-4 mt-4">
                        <button type="button" onClick={() => setIsDeleteMaterial(false)} className="px-4 py-2 text-gray-500 hover:text-gray-900">Отмена</button>
                        <button type="submit" disabled={loading} className="px-6 py-2 bg-red-500 hover:bg-red-600 text-white text-sm font-medium py-2.5 px-4 rounded-xl transition-all duration-200 border border-red-200 hover:shadow-sm">Удалить материал</button>
                    </div>
                </form>
            </Modal>
        </div>
    );
}