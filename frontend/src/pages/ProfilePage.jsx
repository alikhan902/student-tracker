import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { userService } from '../api';
import { Modal } from '../components/ui/Modal';
import { User, Mail, Shield, Key, Trash2 } from 'lucide-react';
import toast from 'react-hot-toast';

export default function ProfilePage() {
  const { user, setUser, logout } = useAuth();
  
  // Update Profile State
  const [isEditProfileModalOpen, setIsEditProfileModalOpen] = useState(false);
  const [profileData, setProfileData] = useState({ name: user?.name || '' });
  
  // Change Password State
  const [isPasswordModalOpen, setIsPasswordModalOpen] = useState(false);
  const [passwordData, setPasswordData] = useState({ currentPassword: '', newPassword: '', confirmPassword: '' });

  // Email Change State
  const [newEmail, setNewEmail] = useState('');

  // Forgot Password State
  const [isForgotPasswordModalOpen, setIsForgotPasswordModalOpen] = useState(false);
  const [forgotPasswordEmail, setForgotPasswordEmail] = useState('');

  // Delete Account State
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [deletePassword, setDeletePassword] = useState('');

  const [loading, setLoading] = useState(false);

  const getRoleColor = (role) => {
    switch(role) {
      case "STUDENT": return 'bg-amber-500/20 text-amber-500 border-amber-500/30';
      case "HEADMEN": return 'bg-blue-500/20 text-blue-500 border-blue-500/30';
      default: return 'bg-gray-500/20 text-gray-500 border-gray-500/30';
    }
  };

  const getProviderColor = (provider) => {
    switch(provider) {
      case 'LOCAL': return 'bg-gray-200 text-gray-700';
      case 'GOOGLE': return 'bg-blue-600 text-white';
      case 'GITHUB': return 'bg-purple-600 text-white';
      default: return 'bg-gray-200 text-gray-700';
    }
  };

  const handleUpdateProfile = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await userService.updateProfile(profileData);
      setUser({ ...user, name: profileData.name });
      toast.success('Профиль успешно обновлен');
      setIsEditProfileModalOpen(false);
    } catch (error) {
      toast.error(error.response?.data?.message || 'Не удалось обновить профиль');
    } finally {
      setLoading(false);
    }
  };

  const handleRequestEmailChange = async (e) => {
    e.preventDefault();
    if (!newEmail) return toast.error('Введите новый email');
    setLoading(true);
    try {
      await userService.requestEmailChange(newEmail);
      toast.success('Ссылка подтверждения отправлена на новый email');
      setNewEmail('');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Не удалось запросить смену email');
    } finally {
      setLoading(false);
    }
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    if (passwordData.newPassword.length < 6) {
      return toast.error('Новый пароль должен содержать минимум 6 символов');
    }
    if (passwordData.newPassword !== passwordData.confirmPassword) {
      return toast.error('Пароли не совпадают');
    }
    setLoading(true);
    try {
      await userService.changePassword(passwordData);
      toast.success('Пароль успешно изменен');
      setIsPasswordModalOpen(false);
      setPasswordData({ currentPassword: '', newPassword: '', confirmPassword: '' });
    } catch (error) {
      toast.error(error.response?.data?.message || 'Не удалось изменить пароль');
    } finally {
      setLoading(false);
    }
  };

  const handleForgotPassword = async (e) => {
    e.preventDefault();
    if (!forgotPasswordEmail) return toast.error('Введите ваш email');
    setLoading(true);
    try {
      await userService.forgotPassword(forgotPasswordEmail);
      toast.success('Ссылка для сброса пароля отправлена на ваш email');
      setIsForgotPasswordModalOpen(false);
      setForgotPasswordEmail('');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Не удалось отправить ссылку для сброса');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteAccount = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await userService.deleteAccount(deletePassword || undefined);
      toast.success('Аккаунт успешно удален');
      logout();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Не удалось удалить аккаунт');
    } finally {
      setLoading(false);
      setIsDeleteModalOpen(false);
    }
  };

  if (!user) return null;

  return (
    <div className="space-y-6 max-w-4xl slide-up">
      <h1 className="text-2xl font-bold font-heading text-gray-900">Профиль аккаунта</h1>

      <div className="bg-white border border-lightblue-border rounded-xl p-6 sm:p-8 shadow-sm">
        <div className="flex flex-col sm:flex-row items-start sm:items-center gap-6">
          <div className="w-24 h-24 rounded-full bg-primary flex items-center justify-center text-4xl text-white font-bold uppercase shrink-0 border-4 border-white">
            {user.name?.charAt(0) || user.username?.charAt(0) || '?'}
          </div>
          
          <div className="flex-1">
            <h2 className="text-2xl font-bold text-gray-900 mb-1">{user.name}</h2>
            <p className="text-primary mb-4 text-sm font-medium">@{user.username}</p>
            
            <div className="flex flex-wrap gap-2 text-sm text-gray-600">
              <span className="flex items-center bg-lightblue-surface px-3 py-1.5 rounded-lg border border-lightblue-border">
                <Mail className="w-4 h-4 mr-2 text-gray-500" /> {user.email || 'Email не указан'}
              </span>
              <span className={`px-3 py-1.5 rounded-lg text-xs font-bold uppercase tracking-wide flex items-center ${getProviderColor(user.providerType || 'LOCAL')}`}>
                {user.providerType || 'LOCAL'}
              </span>
              <span className={`px-3 py-1.5 rounded-lg text-xs font-bold uppercase tracking-wide flex items-center ${getRoleColor(user.studentType)}`}>
                {user.studentType === 0 ? 'Пользователь' : user.studentType === 1 ? 'Администратор' : 'Роль не указана'}
              </span>
              <Shield className={`w-4 h-4 text-gray-500 ${getRoleColor(user.studentType)}`} onClick={console.log(user.studentType)}/>
            </div>
          </div>
          
          <div className="flex flex-col items-stretch gap-3 w-full sm:w-auto mt-4 sm:mt-0">
            <button 
              onClick={() => {
                setProfileData({ name: user.name || '' });
                setIsEditProfileModalOpen(true);
              }}
              className="bg-lightblue-surface hover:bg-lightblue-border text-gray-900 text-sm font-medium py-2 px-4 rounded-lg transition-colors border border-lightblue-border flex justify-center items-center"
            >
              <User className="w-4 h-4 mr-2" /> Редактировать профиль
            </button>
            <button 
              onClick={() => setIsPasswordModalOpen(true)}
              className="bg-lightblue-surface hover:bg-lightblue-border text-gray-900 text-sm font-medium py-2 px-4 rounded-lg transition-colors border border-lightblue-border flex justify-center items-center"
            >
              <Key className="w-4 h-4 mr-2" /> Сменить пароль
            </button>
          </div>
        </div>
      </div>

      {/* Change Email Section */}
      <div className="bg-white border border-lightblue-border rounded-xl p-6 sm:p-8 shadow-sm">
        <h3 className="text-lg font-heading font-bold text-gray-900 mb-2">Сменить email</h3>
        <p className="text-gray-500 text-sm mb-4">
          Вы должны подтвердить новый email, перейдя по ссылке, отправленной в ваш почтовый ящик.
        </p>
        <form onSubmit={handleRequestEmailChange} className="flex flex-col sm:flex-row gap-3">
          <input 
            type="email" 
            placeholder="Введите новый адрес email" 
            value={newEmail} 
            onChange={(e) => setNewEmail(e.target.value)} 
            className="flex-1 px-3 py-2 bg-lightblue-surface border border-lightblue-border rounded-lg text-gray-900 focus:outline-none focus:ring-1 focus:ring-primary"
            required 
          />
          <button 
            type="submit" 
            disabled={loading}
            className="bg-primary hover:bg-primary-hover text-white text-sm font-medium py-2 px-4 rounded-lg transition-colors whitespace-nowrap disabled:opacity-50"
          >
            Запросить смену email
          </button>
        </form>
      </div>

      {/* Danger Zone */}
      <div className="bg-lightred-bg border border-red-300 rounded-xl p-6 sm:p-8 shadow-sm">
        <h3 className="text-lg font-heading font-bold text-red-600 mb-2">Опасная зона</h3>
        <p className="text-gray-600 text-sm mb-4">
          После удаления аккаунта восстановить его будет невозможно. Все ваши расходы, доходы и бюджеты будут безвозвратно уничтожены.
        </p>
        <button 
          onClick={() => setIsDeleteModalOpen(true)}
          className="bg-red-500 hover:bg-red-600 text-white text-sm font-medium py-2 px-4 rounded-lg transition-colors flex items-center"
        >
          <Trash2 className="w-4 h-4 mr-2" /> Удалить аккаунт
        </button>
      </div>

      {/* Edit Profile Modal */}
      <Modal isOpen={isEditProfileModalOpen} onClose={() => setIsEditProfileModalOpen(false)} title="Редактировать профиль">
        <form onSubmit={handleUpdateProfile} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Полное имя</label>
            <input type="text" required value={profileData.name} onChange={(e) => setProfileData({...profileData, name: e.target.value})} className="w-full px-3 py-2 bg-lightblue-surface border border-lightblue-border rounded-lg text-gray-900 focus:outline-none focus:ring-1 focus:ring-primary" />
          </div>
          <div className="flex justify-end gap-2 pt-4 border-t border-lightblue-border mt-4">
             <button type="button" onClick={() => setIsEditProfileModalOpen(false)} className="px-4 py-2 text-gray-500 hover:text-gray-900">Отмена</button>
             <button type="submit" disabled={loading} className="px-6 py-2 bg-primary hover:bg-primary-hover text-white rounded-lg disabled:opacity-50 font-medium">Сохранить изменения</button>
          </div>
        </form>
      </Modal>

      {/* Change Password Modal */}
      <Modal isOpen={isPasswordModalOpen} onClose={() => setIsPasswordModalOpen(false)} title="Сменить пароль">
        <form onSubmit={handleChangePassword} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Текущий пароль</label>
            <input type="password" required value={passwordData.currentPassword} onChange={(e) => setPasswordData({...passwordData, currentPassword: e.target.value})} className="w-full px-3 py-2 bg-lightblue-surface border border-lightblue-border rounded-lg text-gray-900 focus:outline-none focus:ring-1 focus:ring-primary" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Новый пароль</label>
            <input type="password" required value={passwordData.newPassword} onChange={(e) => setPasswordData({...passwordData, newPassword: e.target.value})} className="w-full px-3 py-2 bg-lightblue-surface border border-lightblue-border rounded-lg text-gray-900 focus:outline-none focus:ring-1 focus:ring-primary" placeholder="Минимум 6 символов" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Подтвердить новый пароль</label>
            <input type="password" required value={passwordData.confirmPassword} onChange={(e) => setPasswordData({...passwordData, confirmPassword: e.target.value})} className="w-full px-3 py-2 bg-lightblue-surface border border-lightblue-border rounded-lg text-gray-900 focus:outline-none focus:ring-1 focus:ring-primary" />
          </div>
          <div className="flex items-center justify-between pt-4 border-t border-lightblue-border mt-4">
             <button 
                type="button" 
                onClick={() => {
                  setIsPasswordModalOpen(false);
                  setIsForgotPasswordModalOpen(true);
                }} 
                className="text-sm text-primary hover:text-primary-dark hover:underline transition-colors focus:outline-none"
             >
               Забыли пароль?
             </button>
             <div className="flex gap-2">
               <button type="button" onClick={() => setIsPasswordModalOpen(false)} className="px-4 py-2 text-gray-500 hover:text-gray-900 focus:outline-none">Отмена</button>
               <button type="submit" disabled={loading} className="px-6 py-2 bg-primary hover:bg-primary-hover text-white rounded-lg disabled:opacity-50 font-medium focus:outline-none">Обновить</button>
             </div>
          </div>
        </form>
      </Modal>

      {/* Forgot Password Modal */}
      <Modal isOpen={isForgotPasswordModalOpen} onClose={() => setIsForgotPasswordModalOpen(false)} title="Сброс пароля">
        <form onSubmit={handleForgotPassword} className="space-y-4">
          <p className="text-sm text-gray-600">Введите адрес электронной почты, и мы отправим вам ссылку для сброса пароля.</p>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Адрес электронной почты</label>
            <input type="email" required value={forgotPasswordEmail} onChange={(e) => setForgotPasswordEmail(e.target.value)} className="w-full px-3 py-2 bg-lightblue-surface border border-lightblue-border rounded-lg text-gray-900 focus:outline-none focus:ring-1 focus:ring-primary" placeholder="your@email.com" />
          </div>
          <div className="flex justify-end gap-2 pt-4 border-t border-lightblue-border mt-4">
             <button type="button" onClick={() => setIsForgotPasswordModalOpen(false)} className="px-4 py-2 text-gray-500 hover:text-gray-900 focus:outline-none">Отмена</button>
             <button type="submit" disabled={loading} className="px-6 py-2 bg-primary hover:bg-primary-hover text-white rounded-lg disabled:opacity-50 font-medium focus:outline-none">Отправить ссылку</button>
          </div>
        </form>
      </Modal>

      {/* Delete Account Modal */}
      <Modal isOpen={isDeleteModalOpen} onClose={() => setIsDeleteModalOpen(false)} title="Подтверждение удаления аккаунта">
        <form onSubmit={handleDeleteAccount} className="space-y-4">
          <p className="text-sm text-gray-600">
            Введите пароль для подтверждения удаления аккаунта. Если вы регистрировались через Google или GitHub, поле можно оставить пустым.
          </p>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Подтверждение пароля (необязательно для OAuth)</label>
            <input type="password" value={deletePassword} onChange={(e) => setDeletePassword(e.target.value)} className="w-full px-3 py-2 bg-lightblue-surface border border-red-300 rounded-lg text-gray-900 focus:outline-none focus:ring-1 focus:ring-danger" placeholder="Ваш пароль" />
          </div>
          <div className="flex justify-end gap-2 pt-4 border-t border-red-300 mt-4">
             <button type="button" onClick={() => setIsDeleteModalOpen(false)} className="px-4 py-2 text-gray-500 hover:text-gray-900 focus:outline-none">Отмена</button>
             <button type="submit" disabled={loading} className="px-6 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg disabled:opacity-50 font-medium focus:outline-none">Удалить</button>
          </div>
        </form>
      </Modal>

    </div>
  );
}

