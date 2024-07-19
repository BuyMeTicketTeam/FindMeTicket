import {
  busIcon, trainIcon, everythingIcon,
} from '../images/transport-img/img';

export const getIcon = (type) => {
  switch (type) {
    case 'BUS':
      return busIcon;
    case 'TRAIN':
      return trainIcon;
    default:
      return everythingIcon;
  }
};
