import { SubjectCard } from '../components/Subject/SubjectCard';
import toast from 'react-hot-toast';

const SUBJECTS = [
  { title: 'Химия', image: 'https://kimc.ms/soobshchestva/gmo/gmo-chemistry/%D0%91%D0%B0%D0%BD%D0%BD%D0%B5%D1%80.jpeg' },
  { title: 'Физика', image: 'https://upload.wikimedia.org/wikipedia/commons/9/9a/CollageFisica.jpg' },
  { title: 'Программирование', image: 'https://i.ytimg.com/vi/XcuyMgceeNw/maxresdefault.jpg' },
  { title: 'Педагогика', image: 'https://avatars.mds.yandex.net/i?id=bdfe470cf3f3d91f4be2c89f8d2357a0_l-10767168-images-thumbs&n=13' },
  { title: 'Анализ данных', image: 'https://avatars.mds.yandex.net/i?id=4143fd1e32cf121086716f4be8915efe_l-3688950-images-thumbs&n=13' },
];

export default function SubjectSelectionPage() {
 
    return (
        <div className="p-6">
            <h2 className="text-3xl font-heading font-bold text-primary mb-20 text-center">Предметы</h2>
            <div className="grid grid-cols-3 gap-6">
                {SUBJECTS.map((subject) => (
                <SubjectCard title={subject.title} image={subject.image}
                    onClick={() => alert(subject.title)}/>
                ))}
            </div>
        </div>
    );
}