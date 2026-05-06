import { openBrowserAsync } from 'expo-web-browser';
import { type ReactNode } from 'react';
import { Linking, Platform, Pressable, type GestureResponderEvent } from 'react-native';

type Props = {
  href: string;
  children: ReactNode;
};

export function ExternalLink({ href, children }: Props) {
  const handlePress = async (_event: GestureResponderEvent) => {
    if (Platform.OS === 'web') {
      window.open(href, '_blank', 'noopener,noreferrer');
      return;
    }

    const canOpen = await Linking.canOpenURL(href);
    if (canOpen) {
      await openBrowserAsync(href);
    }
  };

  return <Pressable onPress={handlePress}>{children}</Pressable>;
}
