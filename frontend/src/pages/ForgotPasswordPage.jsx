import { useState } from 'react';
import { Link } from 'react-router-dom';
import { userService } from '../api';
import { Mail, ArrowLeft } from 'lucide-react';
import toast from 'react-hot-toast';

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await userService.forgotPassword(email);
      setSubmitted(true);
      toast.success('Ссылка для сброса пароля отправлена на вашу почту.');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Не удалось отправить письмо для сброса.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-white p-4 slide-up">
      <div className="w-full max-w-md bg-white border border-lightblue-border rounded-2xl shadow-xl overflow-hidden">
        <div className="p-8">
          <div className="mb-6">
            <Link to="/login" className="inline-flex items-center text-sm text-gray-600 hover:text-gray-900 transition-colors">
              <ArrowLeft className="w-4 h-4 mr-1" /> Назад ко входу
            </Link>
          </div>
          
          <div className="text-center mb-8">
            <h1 className="text-3xl font-heading font-bold text-gray-900 mb-2">Забыли пароль?</h1>
            <p className="text-gray-500">
              {submitted ? "Проверьте вашу почту для инструкций по сбросу." : "Введите email для получения ссылки на сброс пароля."}
            </p>
          </div>
          
          {!submitted ? (
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Адрес электронной почты</label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <Mail className="h-5 w-5 text-gray-500" />
                  </div>
                  <input
                    type="email"
                    required
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="w-full pl-10 pr-3 py-2 bg-lightblue-surface border border-lightblue-border rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary transition"
                    placeholder="example@email.com"
                  />
                </div>
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full py-2.5 px-4 rounded-lg bg-primary hover:bg-primary-hover text-white font-medium transition-colors disabled:opacity-50 disabled:cursor-not-allowed mt-2"
              >
                {loading ? 'Отправка...' : 'Отправить ссылку для сброса'}
              </button>
            </form>
          ) : (
            <div className="bg-lightblue-surface p-4 rounded-lg border border-lightblue-border text-center">
              <p className="text-primary font-medium mb-4">Письмо для сброса отправлено успешно</p>
              <button
                onClick={() => setSubmitted(false)}
                className="text-sm text-gray-600 hover:text-gray-900 transition-colors underline"
              >
                Отправить снова
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

