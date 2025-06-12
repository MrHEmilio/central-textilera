import { ReactNode } from "react";

export default function StatisticManual({
  title,
  child,
}: {
  title: string;
  child: ReactNode | string;
}) {
  return (
    <>
      <div className="grid grid-cols-1 gap-1">
        <h4 style={{color: '#00000073'}}>{title}</h4>
        <div>
          {child}
        </div>
      </div>
    </>
  );
}
