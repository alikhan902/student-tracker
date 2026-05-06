import { subjectService } from '../api';
import { useAuth } from '../context/AuthContext';
import { SubjectCard } from '../components/Subject/SubjectCard';
import { Plus } from 'lucide-react';
import { useEffect, useState } from 'react';
import { Modal } from '../components/ui/Modal';
import { useNavigate } from 'react-router-dom';

import toast from 'react-hot-toast';

export default function SubjectSelectionPage() {
 
    const { user, setUser, logout } = useAuth();

    const [isCreateSubject, setIsCreateSubject] = useState(false);
    const [isDeleteSubject, setIsDeleteSubject] = useState(false);

    const [subjects, setSubjects] = useState([]);
    const [loading, setLoading] = useState(false);

    const [selectedSubject, setSelectedSubject] = useState(null);

    const [subjectTitle, setSubjectTitle] = useState('');
    const [subjectDescription, setSubjectDescription] = useState('');
    const [subjectFile, setSubjectFile] = useState(null);

    const navigate = useNavigate();

    const fetchSubjects = async () => {
        try{
            const subjects = await subjectService.getSubjects();
            setSubjects(subjects.data);
            if(subjects == null || subjects == undefined) {
            toast.error('Вы не состоите ни в одной группе. Попросите старосту создать группу и добавить вас в неё.');
        }
        } catch (error) {
            console.error("Error fetching subjects:", error?.response?.data?.error ?? error.message);
        }
    };

    useEffect(() => {
        fetchSubjects();
    }, []);

    const handleCreateSubject = (e) => {
        e.preventDefault();
        if(!subjectTitle || !subjectDescription || !subjectFile) {
            toast.error('Пожалуйста, заполните все поля и выберите файл.');
            return;
        }
        if(subjectFile.size > 1 * 1024 * 1024) {
            toast.error('Размер файла не должен превышать 1 МБ.');
            return;
        }
        if(!['image/jpeg', 'image/png'].includes(subjectFile.type)) {
            toast.error('Недопустимый формат файла. Разрешены JPEG и PNG.');
            return;
        }
        setLoading(true);
        try {
            const formData = new FormData();
            const dto = {
                title: subjectTitle,
                description: subjectDescription
            };

            formData.append("data", JSON.stringify(dto));
            formData.append("photo", subjectFile);
            subjectService.createSubject(formData)
        } catch (error) {
            console.error("Error creating subject:", error?.response?.data?.error ?? error.message);
            toast.error('Ошибка при создании предмета. Пожалуйста, попробуйте снова.');
        } finally {
            setLoading(false);
            setIsCreateSubject(false);
        }
    }

    const handleDeleteSubject = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            await subjectService.deleteSubject(selectedSubject.id);
            setSubjects(subjects.filter((s) => s.id !== selectedSubject.id));
            toast.success('Предмет успешно удален.');
        } catch (error) {
            console.error("Error deleting subject:", error?.response?.data?.error ?? error.message);
            toast.error('Ошибка при удалении предмета. Пожалуйста, попробуйте снова.');
        }
        finally {
            setLoading(false);
            setIsDeleteSubject(false);
        }
    };

    return (
        <div className="p-6">
            <h2 className="text-3xl font-heading font-bold text-primary mb-20 text-center">Предметы</h2>
            <div className="grid grid-cols-3 sm:gap-[25px] md:gap-[50px] lg:gap-[75px]">
                {subjects?.map((subject) => (
                <SubjectCard key={subject.id} title={subject.title} url={subject.photoUrl}
                    onClick={() => navigate(`/subjects/${subject.id}`)} isDeleteVisible={user.studentType === "HEADMAN"} onDelete={() => {
                        setSelectedSubject(subject);
                        setIsDeleteSubject(true);
                    }}/>
                ))}

                {user.studentType == "HEADMAN" && 
                <div onClick={() => setIsCreateSubject(true)} className="group relative w-full h-[200px] rounded-2xl overflow-hidden bg-white shadow-md border-[4px] border-dashed border-[rgb(57,135,250)] cursor-pointer hover:border-[rgb(0,100,200)] duration-300">
                    <Plus className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 text-[rgb(57,135,250)] group-hover:text-[rgb(0,100,200)] duration-300" size={48}/>
                </div>}
            </div>

            <Modal isOpen={isCreateSubject} onClose={() => setIsCreateSubject(false)} title="Создать предмет">
                <form onSubmit={(e) => handleCreateSubject(e)} className="space-y-4">
                    <div>
                        <label className="block text-gray-700 mb-5 text-center sm:text-[14px] md:text-[16px] lg:text-[18px]">Название предмета</label>
                        <input type="text" placeholder="Введите название предмета" className="border border-gray-300 w-full rounded-md py-2 px-4 focus:outline-none focus:ring-2 focus:ring-blue-500" value={subjectTitle} onChange={(e) => setSubjectTitle(e.target.value)}/>
                        <label className="block text-gray-700 mb-5 text-center sm:text-[14px] md:text-[16px] lg:text-[18px]">Описание предмета</label>
                        <textarea placeholder="Введите описание предмета" className="border border-gray-300 w-full min-h-[50px] max-h-[200px] rounded-md py-2 px-4 focus:outline-none focus:ring-2 focus:ring-blue-500" value={subjectDescription} onChange={(e) => setSubjectDescription(e.target.value)}/>
                        <label className="block text-gray-700 mb-5 text-center sm:text-[14px] md:text-[16px] lg:text-[18px]">Фото предмета</label>
                        <div className="flex flex-col items-center gap-3">
                            <label className="cursor-pointer bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-6 rounded-lg shadow-md transition duration-200">
                                Выбрать файл
                                <input
                                type="file"
                                className="hidden"
                                onChange={(e) => setSubjectFile(e.target.files[0])}
                                />
                            </label>

                            {subjectFile && (
                            <span className="text-sm text-gray-600">
                                {subjectFile.name}
                            </span>
                            )}
                        </div>
                    </div>
                    <div className="flex justify-end gap-2 pt-4 mt-4">
                        <button type="button" onClick={() => setIsCreateSubject(false)} className="px-4 py-2 text-gray-500 hover:text-gray-900">Отмена</button>
                        <button type="submit" disabled={loading} className="px-6 py-2 bg-blue-50 hover:bg-blue-100 text-blue-600 text-sm font-medium py-2.5 px-4 rounded-xl transition-all duration-200 border border-blue-200 hover:shadow-sm">Создать предмет</button>
                    </div>
                </form>
            </Modal>

            <Modal isOpen={isDeleteSubject} onClose={() => setIsDeleteSubject(false)} title="Удаление предмета">
                <form onSubmit={(e) => handleDeleteSubject(e)} className="space-y-4">
                    <div>
                        <h2 className="block text-gray-700 mb-5 text-center sm:text-[14px] md:text-[16px] lg:text-[18px]">Удаление предмета</h2>
                        <p className="block text-gray-700 mb-5 text-center sm:text-[14px] md:text-[16px] lg:text-[18px]">Вы уверены, что хотите удалить этот предмет?</p>
                    </div>
                    <div className="flex justify-end gap-2 pt-4 mt-4">
                        <button type="button" onClick={() => setIsDeleteSubject(false)} className="px-4 py-2 text-gray-500 hover:text-gray-900">Отмена</button>
                        <button type="submit" disabled={loading} className="px-6 py-2 bg-red-500 hover:bg-red-600 text-white text-sm font-medium py-2.5 px-4 rounded-xl transition-all duration-200 border border-red-200 hover:shadow-sm">Удалить предмет</button>
                    </div>
                </form>
            </Modal>
        </div>
    );
}