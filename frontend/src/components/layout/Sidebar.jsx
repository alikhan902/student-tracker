import { NavLink } from 'react-router-dom';
import { UserCircle } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

const NAV_ITEMS_PROFILE = [
  { path: '/profile', label: 'Профиль', icon: UserCircle },
  { path: '/subjects', label: 'Предметы', icon: UserCircle },
  { path: '/groups', label: 'Группа', icon: UserCircle },
];

export const Sidebar = ({ isOpen, setMobileMenuOpen }) => {
  const { user, logout } = useAuth();

  return (
    <>
      <div 
        className={`fixed inset-y-0 left-0 z-20 w-64 bg-white border-r border-lightblue-border transform transition-transform duration-300 ease-in-out ${isOpen ? 'translate-x-0' : '-translate-x-full'}`}
      >
        <div className="flex flex-col h-full">
          <div className="flex items-center justify-center h-16 border-b border-lightblue-border px-4">
            <h1 className="text-xl font-heading font-bold text-primary">Список групп</h1>
          </div>
          
          <nav className="flex-1 px-4 py-6 space-y-2 overflow-y-auto scrollbar-custom">
            {NAV_ITEMS_PROFILE.map((item) => {
              const Icon = item.icon;
              return (
                <NavLink
                  key={item.path}
                  to={item.path}
                  onClick={() => setMobileMenuOpen(false)}
                  className={({ isActive }) =>
                    `flex items-center px-4 py-3 rounded-lg transition-colors ${
                      isActive 
                        ? 'bg-primary text-white' 
                        : 'text-gray-700 hover:bg-lightblue-50 hover:text-gray-900'
                    }`
                  }
                >
                  <Icon className="w-5 h-5 mr-3" />
                  <span className="font-medium">{item.label}</span>
                </NavLink>
              );
            })}
          </nav>

          <div className="p-4 border-t border-lightblue-border">
            <div className="flex items-center mb-4 px-2">
              <div className="w-8 h-8 rounded-full bg-primary flex items-center justify-center text-white font-bold uppercase shrink-0">
                {user?.name?.charAt(0) || user?.username?.charAt(0) || '?'}
              </div>
              <div className="ml-3 truncate">
                <p className="text-sm font-medium text-gray-900 truncate">{user?.name}</p>
                <p className="text-xs text-gray-500 truncate">@{user?.username}</p>
              </div>
            </div>
            <button 
              onClick={logout}
              className="w-full py-2 px-4 bg-lightblue-surface hover:bg-red-500 hover:text-white text-gray-900 rounded-lg transition-colors text-sm font-medium"
            >
              Выход
            </button>
          </div>
        </div>
      </div>
      
      {/* Mobile overlay */}
      {isOpen && (
        <div 
          className="fixed inset-0 z-10 bg-black/50"
          onClick={() => setMobileMenuOpen(false)}
        />
      )}
    </>
  );
};

