import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { GroupStudentCard } from '../../group/GroupStudentCard';

describe('GroupStudentCard', () => {
  it('shows delete button for HEADMAN viewer', () => {
    const onDelete = vi.fn();
    render(<GroupStudentCard id={1} name="Ivan" username="ivan" studentType="STUDENT" thisStudentType="HEADMAN" onDeleteUser={onDelete} />);
    const btn = screen.getByText('Удалить');
    expect(btn).toBeTruthy();
    fireEvent.click(btn);
    expect(onDelete).toHaveBeenCalled();
  });
});
