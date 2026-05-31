import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { SubjectSection } from '../SubjectSection';

vi.mock('../../../api/index', () => ({
  materialCompleteService: {
    getStatus: vi.fn(() => Promise.resolve({ data: { status: 'NOT_COMPLETED' } })),
    updateStatus: vi.fn(() => Promise.resolve({ data: { status: 'COMPLETED' } })),
  }
}));

vi.mock('axios', () => ({
  default: {
    get: vi.fn(() => Promise.resolve({ data: new Blob(['ok']) })),
    create: () => ({ get: vi.fn(() => Promise.resolve({ data: new Blob(['ok']) })) })
  }
}));

describe('SubjectSection', () => {
  it('renders and toggles completion and download', async () => {
    render(<SubjectSection title="S" description="D" originalFileName="f.txt" fileDownloadUrl="/file" materialId={1} isSettingsVisible={false} />);

    // open
    fireEvent.click(screen.getByText('S'));

    // click download
    const dl = await screen.findByText('Загрузить');
    fireEvent.click(dl);

    // toggle completion
    const toggle = await screen.findByText(/Не выполнено|Выполнено/);
    fireEvent.click(toggle);

    await waitFor(() => expect(toggle.textContent).toMatch(/Выполнено|Не выполнено/));
  });
});
