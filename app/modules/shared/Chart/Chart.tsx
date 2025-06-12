import React from 'react';
import { Line } from 'react-chartjs-2';

import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const options = {
  responsive: true,
  scales: {
    yAxes: { ticks: { display: false } },
    xAxes: { ticks: { display: false } }
  },
  plugins: {
    legend: {
      position: 'top' as const,
      display: false
    },
    title: {
      display: false,
      text: 'Chart.js Line Chart'
    }
  }
};

const labels = ['en', 'feb', 'mar', 'abr', 'may', 'jun', 'jul'];

const data = {
  labels,
  datasets: [
    {
      data: [40, 30, 28, 38, 40, 50, 45],
      borderColor: '#006eb2'
    }
  ]
};

export const Chart = () => {
  return (
    <div style={{ width: '200px' }}>
      <Line options={options} data={data} />
    </div>
  );
};
