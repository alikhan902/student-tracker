import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { SubjectCard } from '../SubjectCard';

describe('SubjectCard', () => {
  it('renders title and image and delete button when visible', () => {
    const onClick = vi.fn();
    const onDelete = vi.fn();
    render(<SubjectCard title="Math" url="/img.png" onClick={onClick} isDeleteVisible={true} onDelete={onDelete} />);
    expect(screen.getByText('Math')).toBeInTheDocument();
    const img = document.querySelector('img');
    expect(img).toBeTruthy();
    const btn = screen.getByText('Удалить предмет');
    expect(btn).toBeTruthy();
    fireEvent.click(btn);
    expect(onDelete).toHaveBeenCalled();
  });
});
