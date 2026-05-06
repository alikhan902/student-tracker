import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';
import { groupService } from '../api';
import { GroupStudentCard } from '../components/group/GroupStudentCard';
import { Modal } from '../components/ui/Modal';
import { useNavigate } from 'react-router-dom';

export default function GroupPage() {
 
    const { user, setUser, logout } = useAuth();
    const [group, setGroup] = useState(null);
    const [selectedUser, setSelectedUser] = useState(null);
    const [addUserUsername, setAddUserUsername] = useState('');
    const [isAddUserModalOpen, setIsAddUserModalOpen] = useState(false);
    const [isDeleteGroupModal, setIsDeleteGroupModal] = useState(false);
    const [isDeleteUserModal, setIsDeleteUserModal] = useState(false);
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const fetchGroup = async () => {
        try{
            const group = await groupService.getMyGroup();
            setGroup(group.data);
            if(group == null || group == undefined) {
            toast.error('Вы не состоите ни в одной группе. Попросите старосту создать группу и добавить вас в неё.');
        }
        } catch (error) {
            console.error("Error fetching group:", error.response?.data?.error);
        }
    };


    useEffect(() => {
        fetchGroup();
    }, []);

    const handleDeleteUser = async (e, id) => {
        setLoading(true);
        e.preventDefault();
        try {
            await groupService.deleteUserFromGroup(id);
            toast.success('Пользователь успешно удален из группы');
        } catch (error) {
            toast.error(error.response?.data?.error || 'Не удалось удалить пользователя из группы');
        } 
        finally {
            setLoading(false);
            setIsDeleteUserModal(false);
        }
        fetchGroup();
    };

    const handleAddUser = async (e) => {
        setLoading(true);
        e.preventDefault();
        try {
            await groupService.addUserToGroup(addUserUsername);
            toast.success('Пользователь успешно добавлен в группу');
        } catch (error) {
            toast.error(error.response?.data?.error || 'Не удалось добавить пользователя в группу');
        } finally {
            setLoading(false);
            setIsAddUserModalOpen(false);
        }
        fetchGroup();
    }

    const handleDeleteGroup = async (e) => {
        setLoading(true);
        e.preventDefault();
        let success = false;
        try {
            success = await groupService.deleteGroup();
            toast.success('Группа успешно удалена');
        } catch (error) {
            toast.error(error.response?.data?.error || 'Не удалось удалить группу');
            return;
        } finally {
            setIsDeleteGroupModal(false);
            setLoading(false);
        }

        if(success) {
            navigate('/profile');
        }
    }

    return (
        <div className="flex gap-6">
            <div className="w-3/4 mx-auto">
                <h2 className="text-3xl font-heading font-bold text-primary mb-20 text-center"> {group == null || group == undefined? "Вы не состоите в группе" : "Ваша группа: " + group.name}</h2>
                <div className="flex w-full flex-col gap-6">
                    {group?.members?.map(student => (
                        <GroupStudentCard key={student.id} id={student.id} name={student.name} username={student.username} studentType={student.studentType} thisStudentType={user.studentType} onDeleteUser={() => {setSelectedUser(student); setIsDeleteUserModal(true);}} disabled={loading}/>
                    ))}
                </div>
            </div>
            {user.studentType == "HEADMAN" && (group != null || group != undefined) && (
                <aside className="w-1/4 hidden lg:block">
                    <div className="bg-white shadow-md rounded-2xl p-6 flex flex-col gap-5 border border-lightblue-border">
                        <h2 className="text-lg font-semibold text-gray-800 text-center">
                            Управление группой
                        </h2>

                        <div className="flex flex-col gap-3">
                            <button
                                onClick={() => setIsAddUserModalOpen(true)}
                                className="w-full bg-lightblue-surface hover:bg-lightblue-border text-gray-900 text-sm font-medium py-2.5 px-4 rounded-xl transition-all duration-200 border border-lightblue-border hover:shadow-sm"
                            >
                                Добавить участника
                            </button>

                            <button
                                onClick={() => setIsDeleteGroupModal(true)}
                                className="w-full bg-red-50 hover:bg-red-100 text-red-600 text-sm font-medium py-2.5 px-4 rounded-xl transition-all duration-200 border border-red-200 hover:shadow-sm"
                            >
                                Удалить группу
                            </button>
                        </div>
                    </div>
                </aside>
            )}

            <Modal isOpen={isAddUserModalOpen} onClose={() => setIsAddUserModalOpen(false)} title="Добавить пользователя">
                <form onSubmit={handleAddUser} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Имя пользователя</label>
                        <input type="text" required value={addUserUsername} onChange={(e) => setAddUserUsername(e.target.value)} className="w-full px-3 py-2 bg-lightblue-surface border border-lightblue-border rounded-lg text-gray-900 focus:outline-none focus:ring-1 focus:ring-primary" />
                    </div>
                    <div className="flex justify-end gap-2 pt-4 mt-4">
                        <button type="button" onClick={() => setIsAddUserModalOpen(false)} className="px-4 py-2 text-gray-500 hover:text-gray-900">Отмена</button>
                        <button type="submit" disabled={loading} className="px-6 py-2 bg-primary hover:bg-primary-hover text-white rounded-lg disabled:opacity-50 font-medium">Добавить пользователя</button>
                    </div>
                </form>
            </Modal>

            <Modal isOpen={isDeleteGroupModal} onClose={() => setIsDeleteGroupModal(false)} title="Удалить группу">
                <form onSubmit={handleDeleteGroup} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1 text-center">Вы уверены, что хотите удалить группу?</label>
                    </div>
                    <div className="flex justify-end gap-2 pt-4 mt-4">
                        <button type="button" onClick={() => setIsDeleteGroupModal(false)} className="px-4 py-2 text-gray-500 hover:text-gray-900">Отмена</button>
                        <button type="submit" disabled={loading} className="px-6 py-2 bg-red-50 hover:bg-red-100 text-red-600 text-sm font-medium py-2.5 px-4 rounded-xl transition-all duration-200 border border-red-200 hover:shadow-sm">Удалить группу</button>
                    </div>
                </form>
            </Modal>

            <Modal isOpen={isDeleteUserModal} onClose={() => setIsDeleteUserModal(false)} title="Удалить пользователя">
                <form onSubmit={(e) => handleDeleteUser(e, selectedUser.id)} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1 text-center">Вы уверены, что хотите удалить пользователя?</label>
                    </div>
                    <div className="flex justify-end gap-2 pt-4 mt-4">
                        <button type="button" onClick={() => setIsDeleteUserModal(false)} className="px-4 py-2 text-gray-500 hover:text-gray-900">Отмена</button>
                        <button type="submit" disabled={loading} className="px-6 py-2 bg-red-50 hover:bg-red-100 text-red-600 text-sm font-medium py-2.5 px-4 rounded-xl transition-all duration-200 border border-red-200 hover:shadow-sm">Удалить пользователя</button>
                    </div>
                </form>
            </Modal>
        </div>
    );
}