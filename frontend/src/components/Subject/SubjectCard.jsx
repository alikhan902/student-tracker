import toast from 'react-hot-toast';

export const SubjectCard = ({ title, image, onClick }) => {
  return (
    <div onClick={onClick} className="group relative w-[280px] h-[200px] rounded-2xl overflow-hidden bg-white shadow-md hover:shadow-xl transition-all duration-300 cursor-pointer hover:-translate-y-1">
      
      <img
        src={image}
        alt={title}
        className="absolute inset-0 w-full h-full object-cover group-hover:scale-110 transition-transform duration-500"
      />

      <div className="absolute inset-0 bg-gradient-to-t from-black/70 via-black/30 to-transparent" />

      <div className="relative z-10 h-50 top-[138px] flex items-end p-4 bg-[rgba(0,0,0,0.5)]">
        <h2 className="text-white text-lg font-semibold">
          {title}
        </h2>
      </div>
    </div>
  );
};