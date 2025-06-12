/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  output: 'standalone',
  swcMinify: true,
  images: {
    domains: ['206.189.60.99', 'textile-center-services.com']
  },
  experimental: {
    isrMemoryCacheSize: 0
  },
  headers: async () => [
    {
      source: '/products/',
      headers: [
        {
          key: 'Cache-Control',
          value: 'no-store'
        }
      ]
    }
  ]
};

module.exports = nextConfig;
