import { FC } from 'react';
import { CardTestimonial } from '../CardTestimonial';
import { CarruselCard } from '../CarruselCard';

export const Testimonials: FC = () => {
  const testimonials = [...Array(10)].map((_, i) => i);
  return (
    <div className="container mx-auto mb-3">
      <CarruselCard title="Testimoniales">
        {testimonials.map(i => (
          <div key={i} className=" m-8">
            <CardTestimonial />
          </div>
        ))}
      </CarruselCard>
    </div>
  );
};
