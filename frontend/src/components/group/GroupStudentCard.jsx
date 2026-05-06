import toast from 'react-hot-toast';

export const GroupStudentCard = ({ id, name, username, studentType, thisStudentType, onDeleteUser }) => {
  return (
    <div className="group w-full rounded-2xl bg-white shadow-md hover:shadow-xl transition-all duration-300 hover:-translate-y-1 overflow-hidden border border-gray-100">
      <div className="p-4 flex flex-col gap-2">
        <h2 className="text-gray-900 text-lg font-semibold truncate">
          {name}
        </h2>

        <p className="text-gray-500 text-sm">
          @{username}
        </p>

       {studentType != "HEADMAN" && thisStudentType == "HEADMAN" && <button
          onClick={(e) => {
            if (onDeleteUser) onDeleteUser(e, id);
            else toast.error('Удаление недоступно');
          }}
          className="mt-2 opacity-0 group-hover:opacity-100 transition-all duration-200 text-sm text-red-500 hover:text-red-600 self-start"
        >
          Удалить
        </button>}
      </div>
    </div>
  );
};