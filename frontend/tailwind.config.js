/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        lightwhite: {
          bg: '#FFFFFF',
          card: '#F8FAFC',
          border: '#E2E8F0',
          surface: '#F1F5F9'
        },
        lightblue: {
          bg: '#F0F9FF',
          card: '#E0F2FE',
          border: '#BDE4FE',
          surface: '#C7E5FF'
        },
        lightred: {
          bg: '#FEF2F2',
          border: '#FECACA'
        },
        primary: {
          DEFAULT: '#3B82F6',
          hover: '#2563EB',
          light: '#60A5FA'
        },
        danger: '#EF4444',
        warning: '#F59E0B',
        success: '#10B981',
      },
      fontFamily: {
        heading: ['Syne', 'sans-serif'],
        body: ['DM Sans', 'sans-serif'],
      }
    },
  },
  plugins: [],
}

