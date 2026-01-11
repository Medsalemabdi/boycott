FROM node:18-alpine

# Create non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy only dependency files first (better cache)
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy application source
COPY . .

# Build-time environment variable
ARG REACT_APP_API_URL
ENV REACT_APP_API_URL=$REACT_APP_API_URL

# Build the React app
RUN npm run build

# Install serve locally (no global install needed)
RUN npm install serve

# Change ownership and switch user
RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 3001

CMD ["npx", "serve", "-s", "build", "-l", "3001"]
