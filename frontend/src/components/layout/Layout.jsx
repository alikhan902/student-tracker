import { useState } from 'react';
import { Outlet } from 'react-router-dom';
import { Menu } from 'lucide-react';
import { Sidebar } from './Sidebar';

export const Layout = () => {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  return (
    <div className="flex h-screen bg-white text-gray-900 overflow-hidden font-body">
      <Sidebar isOpen={mobileMenuOpen} setMobileMenuOpen={setMobileMenuOpen} />
      
      <div className={`flex-1 flex flex-col relative`}>
        <header className="bg-white border-b border-lightblue-border h-16 flex items-center px-4 shrink-0">
          <button 
            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
            className="text-gray-700 hover:text-gray-900 p-1"
          >
            <Menu className="w-6 h-6" />
          </button>
          <span className="ml-4 font-heading font-bold text-lg text-primary">Student Tracker</span>
        </header>

        <main className="flex-1 overflow-auto scrollbar-custom p-4 md:p-8">
          <div className="max-w-7xl mx-auto h-full slide-up">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
};

