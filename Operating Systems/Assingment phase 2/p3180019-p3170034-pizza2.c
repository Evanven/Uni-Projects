#include  <p3180019-p3170034-pizza2.h>
int temp_oid = -1;
int latency;
int wait_time;

int status = 1;
int seed;

int cooks=0;
int ovens=0;
int deli=0;

static pthread_mutex_t uCooks;
static pthread_mutex_t uOvens;
static pthread_mutex_t uDeli;
static pthread_mutex_t uLatency;

void acquire_cook() {
  while(cooks==nCook) {
    //waiting for a Cook to be available
  }
  pthread_mutex_lock(&uCooks);
  cooks+=1;
  pthread_mutex_unlock(&uCooks);
}

void acquire_oven() {
  while(ovens==nOven) {
    //waiting for an Oven to be available
  }
  pthread_mutex_lock(&uOvens);
  ovens+=1;
  pthread_mutex_unlock(&uOvens);
}

void acquire_deli(){
  while(deli==nDeli) {
    //waiting for an Deliverer to be available
  }
  pthread_mutex_lock(&uDeli);
  deli+=1;
  pthread_mutex_unlock(&uDeli);
}

void release_cook() {
  pthread_mutex_lock(&uCooks);
  cooks-=1;
  pthread_mutex_unlock(&uCooks);
}

void release_oven() {
  pthread_mutex_lock(&uOvens);
  ovens-=1;
  pthread_mutex_unlock(&uOvens);
}

void release_deli() {
  pthread_mutex_lock(&uDeli);
  deli-=1;
  pthread_mutex_unlock(&uDeli);
}

void* order(void* arg) {
  pthread_mutex_lock(&uLatency);
  struct timespec start, end, finished;
  if(clock_gettime(CLOCK_REALTIME, &start)==-1) {
    printf("Error: Clock");
    return &status;
  }
  pthread_mutex_unlock(&uLatency);

  int* pizzas = (int*)arg;
  int oid = temp_oid+1;
  temp_oid = oid;

  int t_time = 0;
  while(t_time<tL) {
    t_time=rand_r(&seed)%(tH+1);
  }

  acquire_cook();
  sleep(tPrep*(*pizzas));

  acquire_oven();
  release_cook();

  sleep(tBake);

  pthread_mutex_lock(&uLatency);
  if(clock_gettime(CLOCK_REALTIME, &finished)==-1) {
    printf("Error: Clock");
    return &status;
  }
  pthread_mutex_unlock(&uLatency);

  acquire_deli();
  release_oven();

  sleep(t_time);

  pthread_mutex_lock(&uLatency);
  if(clock_gettime(CLOCK_REALTIME, &end)==-1) {
    printf("Error: Clock");
    return &status;
  }

  int latency = (int)(end.tv_sec - start.tv_sec);
  int wait_time = (int)(end.tv_sec - finished.tv_sec);
  printf("Η παραγγελία με αριθμό %d, για %d πίτσες, παραδόθηκε σε %d λεπτά και κρύωνε για %d λεπτά\n", oid, *pizzas, latency, wait_time);
  pthread_mutex_unlock(&uLatency);

  sleep(t_time);
  release_deli();

  int latency_digits = floor(log10(abs(latency))) + 1;
  int wait_time_digits = floor(log10(abs(wait_time))) + 1;

  int res = wait_time_digits;
  res += 10*latency_digits;
  res += pow(10,2)*wait_time;
  res += pow(10, wait_time_digits+2)*latency;

  int* ptr = (int *)malloc(sizeof(int));
  *ptr = res;
  return(ptr);
}



int main(int argc, char* argv[]) {
  printf("Έναρξη προγράμματος.\n");

  if(argc!=3) {
    printf("Τα ορισματα πρεπει να ειναι 2. Το πλήθος των πελατών προς εξυπηρέτηση και τον τυχαίο σπόρο για τη γεννήτρια των τυχαίων αριθμών.");
    return 1;
  }

  if(atoi(argv[1])<0) {
    printf("Το πλήθος των πελατών προς εξυπηρέτηση πρέπει να είναι μεγαλύτερο απο 0.");
    return 1;
  }

  int nCust = atoi(argv[1]);
  seed = atoi(argv[2]);

  if (pthread_mutex_init(&uCooks, NULL) != 0) {
    printf("Error: Cook mutex init has failed.\n");
    return 1;
  }

  if (pthread_mutex_init(&uOvens, NULL) != 0) {
      printf("Error: Oven mutex init has failed.\n");
      return 1;
  }

  if (pthread_mutex_init(&uDeli, NULL) != 0) {
    printf("Error: Deliverer mutex init has failed.\n");
    return 1;
  }

  if (pthread_mutex_init(&uLatency, NULL) != 0) {
      printf("Error: Time mutex init has failed.\n");
      return 1;
  }

  pthread_t* threads;
  threads = malloc(nCust*sizeof(pthread_t));
  if(threads==NULL) {
    printf("Error: Memory allocation\n");
    return 1;
  }

  int arguments[nCust];
  int time;

  for(int i=0; i<nCust; i+=1) {

      arguments[i] = 0;
      while(arguments[i]<nOrdL) {
        arguments[i]=rand_r(&seed)%(nOrdH+1);
      }
      pthread_create(&threads[i], NULL, order, &arguments[i]);

      if(i==(nCust-1)) {
        break;
      }

      time = 0;
      while(time<tOrdL) {
        time=rand_r(&seed)%(tOrdH+1);
      }
      sleep(time);
  }

  int max_Latency = 0;
  int max_Travel_time = 0;
  double sumL = 0;
  double sumT = 0;
  int total_lat = 0;
  int total_trav = 0;

  for(int j=0; j<nCust; j+=1) {
    void* void_ptr;
    pthread_join(threads[j],(void *)&void_ptr);
    
    int* value = (int *)void_ptr;

    int num = *value;

    int l_digits = (num%100)/10;
    int t_digits = num%10;

    int ltime = (num/100)/(pow(10, t_digits));
    int ttime = fmod((num/100),(pow(10, t_digits)));

    if(l_digits!=(floor(log10(abs(ltime))) + 1)) {
      printf("Error: Wrong latency\n");
      return 1;
    }
    
    if(t_digits!=(floor(log10(abs(ttime))) + 1)) {
      printf("Error: Wrong travel time\n");
      return 1;
    }
    

    if(ltime >= max_Latency){
        max_Latency = ltime;
      }

      if(ttime >= max_Travel_time){
        max_Travel_time = ttime;
      }

      sumL += ltime;
      sumT += ttime;
  }

  float mvL = sumL/(double)nCust;
  float mvT = sumT/(double)nCust;

  printf("Μέσος χρόνος ολοκλήρωσης παραγγελίας %0.2f\n", mvL);
  printf("Μέγιστος χρόνος ολοκλήρωσης παραγγελίας %d\n", max_Latency);

  printf("Μέσος χρόνος κρυώματος παραγγελίας %0.2f\n", mvT);
  printf("Μέγιστος χρόνος κρυώματος παραγγελίας %d\n", max_Travel_time);

  printf("Λήξη προγράμματος.\n");
  
  free(threads);
  return 0;
}
