import { SubjectSection } from '../components/Subject/SubjectSection';
import toast from 'react-hot-toast';

const SUBJECTS = [
  { title: 'Раздел 1', description: 'Описание раздела 1', originalFileName: 'file1.pdf', fileDownloadUrl: '/downloads/file1.pdf', uploadAt: '2023-01-01' },
  { title: 'Раздел 2', description: 'Описание раздела 2', originalFileName: 'file2.pdf', fileDownloadUrl: '/downloads/file2.pdf', uploadAt: '2023-01-02' },
  { title: 'Раздел 3', description: 'Описание раздела 3', originalFileName: 'file3.pdf', fileDownloadUrl: '/downloads/file3.pdf', uploadAt: '2023-01-03' },
];

export default function SubjectPage() {
 
    return (
        <div className="p-6">
            <h2 className="text-3xl font-heading font-bold text-primary mb-20 text-center">Разделы</h2>
            <div className="flex flex-col gap-6">
                {SUBJECTS.map((subject) => (
                <SubjectSection title={subject.title} description={subject.description} originalFileName={subject.originalFileName} uploadAt={subject.uploadAt} fileDownloadUrl={subject.fileDownloadUrl} />
                ))}
            </div>
        </div>
    );
}