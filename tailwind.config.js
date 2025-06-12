/** @type {import('tailwindcss').Config} */
module.exports = {
  important: true,
  content: ['./pages/**/*.{js,ts,jsx,tsx}', './app/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      backgroundImage: {
        facebook: 'url(/img/iFacebook_blanco.svg)'
      },
      colors: {
        main: '#006eb2',
        enphasis: '#006EB2',
        footercolor: '#002145',
        footerlight: '#dcdcdc',
        secondary: '#2699FB',
        whatsapp: '#1bd741',
        chatbtn: '#006eb2',
        grayPayment: '#F0F0F0',
        graySeparation: '#CCCCCC',
        redalert: '#DD0000',
        paragraph: '#898989'
      },
      animation: {
        'fade-in': 'fadeIn 0.3s linear'
      },
      fontFamily: {
        famBold: ['Mon-Bold', 'arial', 'sans-serif'],
        standar: [
          '-apple-system',
          'BlinkMacSystemFont',
          'Segoe UI',
          'Roboto',
          'Helvetica Neue',
          'Arial',
          'Noto Sans',
          'sans - serif',
          'Apple Color Emoji',
          'Segoe UI Emoji',
          'Segoe UI Symbol',
          'Noto Color Emoji'
        ]
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '50%': { opacity: '0.5' },
          '100%': { opacity: '1' }
        }
      }
    }
  },
  plugins: []
};
